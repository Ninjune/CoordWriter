package dev.ninjune.coordwriter;

import dev.ninjune.coordwriter.commands.ClearFile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import dev.ninjune.coordwriter.commands.StCommand;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CoordWriter implements ModInitializer {
	public static final String PATH = FabricLoader.getInstance().getConfigDir() + "\\coords.txt";
	private static KeyBinding writeKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"Write",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_B,
			"category.coordwriter.coordwriter"
	));

	@Override
	public void onInitialize() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> StCommand.stCommandFunc());
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> ClearFile.clearFileFunc());
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			try {
				while (writeKey.wasPressed()) {
					writeCoords(client);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}


	private void writeCoords(MinecraftClient client) throws IOException {
		int x = (int)Math.floor(client.player.getX());
		int y = (int)Math.floor(client.player.getY());
		int z = (int)Math.floor(client.player.getZ());
		File fileCreator = new File(PATH);
		if (fileCreator.createNewFile()) {
			System.out.println("File created: " + fileCreator.getName());
		}

		FileWriter coordsFile = new FileWriter(PATH, true);
		coordsFile.write("" + x + " " + y + " " + z + "\r\n");
		coordsFile.close();
		client.player.sendMessage(Text.literal("Written " + x + " " + y + " " + z));
	}
}
