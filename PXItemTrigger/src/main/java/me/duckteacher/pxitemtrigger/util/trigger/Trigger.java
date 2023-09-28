package me.duckteacher.pxitemtrigger.util.trigger;

import me.duckteacher.pxitemtrigger.PXItemTrigger;
import me.duckteacher.pxitemtrigger.file.DataManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public class Trigger {
    public static HashSet<Trigger> triggers = new HashSet<>();

    public String name;
    public ItemStack item;
    private HashMap<TriggerType, TriggerCommand> commands;

    public Trigger(String name) {
        this.name = name;
        this.commands = new HashMap<>();
    }

    public @Nullable TriggerCommand getCommand(TriggerType type) { return commands.getOrDefault(type, null); }

    public void setCommand(TriggerType type, @Nullable TriggerCommand command) {
        if (command == null) commands.remove(type);
        else commands.put(type, command);
    }



    public static void setup() {
        FileConfiguration configuration = DataManager.dataFile_triggers;

        for (String triggerName : configuration.getKeys(false)) {
            ConfigurationSection section = configuration.getConfigurationSection(triggerName);
            if (section == null)
                continue;

            Trigger trigger = new Trigger(triggerName);
            trigger.item = section.getItemStack("item");

            ConfigurationSection section_cmd = section.getConfigurationSection("commands");
            if (section_cmd != null) {
                for (String typeName : section_cmd.getKeys(false)) {
                    ConfigurationSection section_cmd_now = section_cmd.getConfigurationSection(typeName);
                    if (section_cmd_now == null)
                        continue;

                    if (!section_cmd_now.contains("force"))
                        continue;

                    boolean force = section_cmd_now.getBoolean("force");
                    String command = section_cmd_now.getString("command");
                    if (command == null)
                        continue;

                    TriggerCommand triggerCommand = new TriggerCommand(command, force);

                    try {
                        TriggerType type = TriggerType.valueOf(typeName.toUpperCase());
                        trigger.setCommand(type, triggerCommand);
                    } catch (IllegalArgumentException ignore) {}
                }
            }

            Trigger.triggers.add(trigger);
        }
    }

    public static void save() {
        FileConfiguration configuration = DataManager.dataFile_triggers;

        for (String key : configuration.getKeys(false))
            configuration.set(key, null);

        for (Trigger trigger : Trigger.triggers) {
            String name = trigger.name;
            ItemStack item = trigger.item;

            ConfigurationSection section = configuration.createSection(name);
            if (item != null)
                section.set("item", item);

            ConfigurationSection section_cmd = section.createSection("commands");
            for (TriggerType type : TriggerType.values()) {
                TriggerCommand triggerCommand = trigger.getCommand(type);
                if (triggerCommand == null)
                    continue;

                String typeName = type.name().toLowerCase();
                ConfigurationSection section_cmd_now = section_cmd.createSection(typeName);

                section_cmd_now.set("force", triggerCommand.force);
                section_cmd_now.set("command", triggerCommand.command);
            }
        }

        try {
            configuration.save(DataManager.file_triggers);
        } catch (IOException e) {
            PXItemTrigger.logger.severe(e.getMessage());
        }
    }

    public static @Nullable Trigger getTrigger(ItemStack item) {
        for (Trigger trigger : triggers) {
            if (trigger.item != null && trigger.item.equals(item))
                return trigger;
        }

        return null;
    }

    public static @Nullable Trigger getTriggerByName(String name) {
        for (Trigger trigger : triggers) {
            if (trigger.name.equals(name))
                return trigger;
        }

        return null;
    }
}
