package me.duckteacher.pxitemtrigger.file.variables;

import me.duckteacher.pxitemtrigger.PXItemTrigger;
import me.duckteacher.pxitemtrigger.file.DataManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Message {
    public static String prefix;

    public static String trigger_created;
    public static String trigger_removed;
    public static String received_item;
    public static String item_set;
    public static String force_set;
    public static String command_set;
    public static String command_removed;
    public static String name_changed;
    public static String config_reloaded;

    public static String err_only_for_player;
    public static String err_page_is_number;
    public static String err_trigger_already_exist;
    public static String err_trigger_not_exist;
    public static String err_name_not_contains_special;
    public static String err_item_not_set;
    public static String err_no_empty_space;
    public static String err_item_already_set;
    public static String err_invalid_type;
    public static String err_force_is_boolean;
    public static String err_command_not_set;
    public static String err_name_already_registered;



    public static void setup() {
        FileConfiguration configuration = DataManager.dataFile_messages;
        
        try {
            prefix = Objects.requireNonNull(configuration.getString("prefix"));

            trigger_created = Objects.requireNonNull(configuration.getString("messages.trigger_created"));
            trigger_removed = Objects.requireNonNull(configuration.getString("messages.trigger_removed"));
            received_item = Objects.requireNonNull(configuration.getString("messages.received_item"));
            item_set = Objects.requireNonNull(configuration.getString("messages.item_set"));
            force_set = Objects.requireNonNull(configuration.getString("messages.force_set"));
            command_set = Objects.requireNonNull(configuration.getString("messages.command_set"));
            command_removed = Objects.requireNonNull(configuration.getString("messages.command_removed"));
            name_changed = Objects.requireNonNull(configuration.getString("messages.name_changed"));
            config_reloaded = Objects.requireNonNull(configuration.getString("messages.config_reloaded"));

            err_only_for_player = Objects.requireNonNull(configuration.getString("messages.error.only_for_player"));
            err_page_is_number = Objects.requireNonNull(configuration.getString("messages.error.page_is_number"));
            err_trigger_already_exist = Objects.requireNonNull(configuration.getString("messages.error.trigger_already_exist"));
            err_trigger_not_exist = Objects.requireNonNull(configuration.getString("messages.error.trigger_not_exist"));
            err_name_not_contains_special = Objects.requireNonNull(configuration.getString("messages.error.name_not_contains_special"));
            err_item_not_set = Objects.requireNonNull(configuration.getString("messages.error.item_not_set"));
            err_no_empty_space = Objects.requireNonNull(configuration.getString("messages.error.no_empty_space"));
            err_item_already_set = Objects.requireNonNull(configuration.getString("messages.error.item_already_set"));
            err_invalid_type = Objects.requireNonNull(configuration.getString("messages.error.invalid_type"));
            err_force_is_boolean = Objects.requireNonNull(configuration.getString("messages.error.force_is_boolean"));
            err_command_not_set = Objects.requireNonNull(configuration.getString("messages.error.command_not_set"));
            err_name_already_registered = Objects.requireNonNull(configuration.getString("messages.error.name_already_registered"));
        } catch (NullPointerException e) {
            PXItemTrigger.getInstance().saveResource("messages.yml", true);
            DataManager.setup();
        }
    }
}
