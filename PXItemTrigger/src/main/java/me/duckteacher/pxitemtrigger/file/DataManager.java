package me.duckteacher.pxitemtrigger.file;

import me.duckteacher.pxitemtrigger.PXItemTrigger;
import me.duckteacher.pxitemtrigger.util.trigger.Trigger;
import me.duckteacher.pxitemtrigger.util.trigger.TriggerCommand;
import me.duckteacher.pxitemtrigger.util.trigger.TriggerType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

public class DataManager {
    private static final File DIR = PXItemTrigger.getInstance().getDataFolder();

    static {
        if (!DIR.exists() || !DIR.isDirectory()) {
            if (!DIR.mkdirs())
                PXItemTrigger.logger.severe("데이터 디렉토리 생성에 실패했습니다.");
        }
    }

    public static void load() {
        File dataFile = new File(DIR, "config.yml");
        YamlConfiguration dataYaml = YamlConfiguration.loadConfiguration(dataFile);
        if (!dataFile.exists() || !dataFile.isFile()) {
            try {
                if (!dataFile.createNewFile()) {
                    PXItemTrigger.logger.severe("콘피그 파일을 생성하지 못했습니다.");
                    return;
                }
            } catch (IOException e) {
                PXItemTrigger.logger.severe("콘피그 파일 생성 도중 IOException이 발생했습니다.");
                return;
            }
        }

        Trigger.triggers = new HashSet<>();

        for (String triggerName : dataYaml.getKeys(false)) {
            ConfigurationSection section = dataYaml.getConfigurationSection(triggerName);
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
                    } catch (IllegalArgumentException ignore) {
                    }

                    Trigger.triggers.add(trigger);
                }
            }
        }
    }

    public static void save() {
        File dataFile = new File(DIR, "config.yml");
        YamlConfiguration dataYaml = YamlConfiguration.loadConfiguration(dataFile);

        for (String key : dataYaml.getKeys(false))
            dataYaml.set(key, null);

        for (Trigger trigger : Trigger.triggers) {
            String name = trigger.name;
            ItemStack item = trigger.item;

            ConfigurationSection section = dataYaml.createSection(name);
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

            try {
                dataYaml.save(dataFile);
            } catch (IOException e) {
                PXItemTrigger.logger.severe("데이터를 저장하는 데 실패했습니다.");
            }
        }

        try {
            dataYaml.save(dataFile);
        } catch (IOException e) {
            PXItemTrigger.logger.severe("콘피그 파일 저장 도중 IOException이 발생했습니다.");
        }
    }
}
