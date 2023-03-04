package dev.ninjune.coordwriter.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.FileWriter;
import java.io.IOException;

import static dev.ninjune.coordwriter.CoordWriter.PATH;

public class ClearFile {
    public static void clearFileFunc() {
        ClientCommandManager.getActiveDispatcher().register(
            ClientCommandManager.literal("clearfile").executes(context -> {
                FileWriter coordsFile = null;
                try {
                    coordsFile = new FileWriter(PATH, false);
                    coordsFile.write("");
                    coordsFile.close();
                    MinecraftClient.getInstance().player.sendMessage(Text.of("Cleared!"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return 0;
            })
        );
    }
}
