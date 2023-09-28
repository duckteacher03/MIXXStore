package me.duckteacher.pxitemtrigger.file;

import me.duckteacher.pxitemtrigger.PXItemTrigger;
import me.duckteacher.pxitemtrigger.file.variables.Message;
import me.duckteacher.pxitemtrigger.util.trigger.Trigger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DataManager {
    public static File file_triggers;
    public static File file_messages;
    public static FileConfiguration dataFile_triggers;
    public static FileConfiguration dataFile_messages;

    public static void setup() {
        final File dataFolder = PXItemTrigger.getInstance().getDataFolder();
        if (!dataFolder.exists() || !dataFolder.isDirectory())
            dataFolder.mkdirs();

        file_triggers = new File(dataFolder, "triggers.yml");
        file_messages = new File(dataFolder, "messages.yml");

        try {
            if (!file_triggers.exists() || !file_triggers.isFile())
                file_triggers.createNewFile();
            if (!file_messages.exists() || !file_messages.isFile())
                file_messages.createNewFile();
        } catch (IOException e) {
            PXItemTrigger.logger.severe(e.getMessage());
            return;
        }

        dataFile_triggers = YamlConfiguration.loadConfiguration(file_triggers);
        dataFile_messages = YamlConfiguration.loadConfiguration(file_messages);


        Trigger.setup();
        Message.setup();
    }

    public static void saveAll() {
        Trigger.save();
    }
}
