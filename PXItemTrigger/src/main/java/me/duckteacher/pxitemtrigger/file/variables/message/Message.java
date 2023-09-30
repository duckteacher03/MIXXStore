package me.duckteacher.pxitemtrigger.file.variables.message;

import me.duckteacher.pxitemtrigger.PXItemTrigger;
import me.duckteacher.pxitemtrigger.file.DataManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;

public class Message {
    public static Message instance;

    public HashMap<MessageKey, String> messageMap;

    public Message(FileConfiguration fileConfiguration) {
        this.messageMap = new HashMap<>();

        for (MessageKey key : MessageKey.values()) {
            String msg = fileConfiguration.getString(key.getKey());
            if (msg == null) {
                PXItemTrigger.getInstance().saveResource("messages.yml", true);
                DataManager.dataFile_messages = YamlConfiguration.loadConfiguration(DataManager.file_messages);
                Message.instance = new Message(DataManager.dataFile_messages);
                break;
            }

            messageMap.put(key, msg);
        }
    }

    public String getMessage(MessageKey key) { return this.messageMap.get(key); }

    public static Message getInstance() {
        return instance;
    }
}
