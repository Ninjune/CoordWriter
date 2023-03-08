package dev.ninjune.coordwriter.commands;


import dev.ninjune.coordwriter.CoordWriter;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static dev.ninjune.coordwriter.CoordWriter.PATH;
import static net.minecraft.client.util.SelectionManager.setClipboard;

public class StCommand {
    public static void stCommandFunc()
    {
        ClientCommandManager.getActiveDispatcher().register(
                ClientCommandManager.literal("stwrite").executes(context -> {
                    String unencodedStImportString = "{\"categories\": [{\"name\": \"imported\",\"waypoints\": [";
                    int index = 1;
                    File coordsFile = new File(PATH);
                    try {
                        if (coordsFile.createNewFile()) {
                            System.out.println("File created: " + coordsFile.getName());
                        } else {
                            System.out.println("File already exists.");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        Scanner scanner = new Scanner(coordsFile);
                        while (scanner.hasNextLine()) {
                            String data = scanner.nextLine();
                            String[] splitData = data.split(" ");
                            unencodedStImportString += "{\"name\": \"" + index++ + "\",\"x\": " + splitData[0] +
                                    ",\"y\": " + splitData[1] + ",\"z\": " + splitData[2] + ",\"enabled\":" +
                                    " \"true\",\"color\": 16711680,\"addedAt\": 1666700977214},";
                        }
                        unencodedStImportString = StringUtils.chop(unencodedStImportString);
                        unencodedStImportString += "],\"island\": \"crystal_hollows\"}]}";
                        setClipboard(MinecraftClient.getInstance(), Base64.encodeBase64String(unencodedStImportString.getBytes()));
                        assert MinecraftClient.getInstance().player != null;
                        MinecraftClient.getInstance().player.sendMessage(Text.of("Copied ST import to clipboard! "));

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return 0;
                })
        );
    }
}
