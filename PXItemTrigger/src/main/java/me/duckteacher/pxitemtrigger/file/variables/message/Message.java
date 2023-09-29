package me.duckteacher.pxitemtrigger.file.variables.message;

import me.duckteacher.pxitemtrigger.file.DataManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

public class Message {
    public static Message instance;

    public HashMap<MessageKey, String> messageMap;

    public Message(FileConfiguration fileConfiguration) {
        FileConfiguration configuration = DataManager.dataFile_messages;
        this.messageMap = new HashMap<>();

        for (MessageKey key : MessageKey.values()) {
            String msg = configuration.getString(key.getKey());
            messageMap.put(key, (msg == null) ? "Invalid Message." : msg);
        }
    }

    public String getMessage(MessageKey key) { return this.messageMap.get(key); }

    public static Message getInstance() {
        return instance;
    }
}
