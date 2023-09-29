package me.duckteacher.pxitemtrigger.file.variables;

import me.duckteacher.pxitemtrigger.PXItemTrigger;
import me.duckteacher.pxitemtrigger.file.DataManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class Message {
    public static Message instance;

    public String prefix;

    public String trigger_created;
    public String trigger_removed;
    public String received_item;
    public String item_set;
    public String force_set;
    public String command_set;
    public String command_removed;
    public String name_changed;
    public String config_reloaded;

    public String err_only_for_player;
    public String err_page_is_number;
    public String err_trigger_already_exist;
    public String err_trigger_not_exist;
    public String err_name_not_contains_special;
    public String err_item_not_set;
    public String err_no_empty_space;
    public String err_item_already_set;
    public String err_invalid_type;
    public String err_force_is_boolean;
    public String err_command_not_set;
    public String err_name_already_registered;


    public Message(FileConfiguration fileConfiguration) {
        FileConfiguration configuration = DataManager.dataFile_messages;

        try {
            this.prefix = Objects.requireNonNull(configuration.getString("prefix"));

            this.trigger_created = Objects.requireNonNull(configuration.getString("messages.trigger_created"));
            this.trigger_removed = Objects.requireNonNull(configuration.getString("messages.trigger_removed"));
            this.received_item = Objects.requireNonNull(configuration.getString("messages.received_item"));
            this.item_set = Objects.requireNonNull(configuration.getString("messages.item_set"));
            this.force_set = Objects.requireNonNull(configuration.getString("messages.force_set"));
            this.command_set = Objects.requireNonNull(configuration.getString("messages.command_set"));
            this.command_removed = Objects.requireNonNull(configuration.getString("messages.command_removed"));
            this.name_changed = Objects.requireNonNull(configuration.getString("messages.name_changed"));
            this.config_reloaded = Objects.requireNonNull(configuration.getString("messages.config_reloaded"));

            this.err_only_for_player = Objects.requireNonNull(configuration.getString("messages.error.only_for_player"));
            this.err_page_is_number = Objects.requireNonNull(configuration.getString("messages.error.page_is_number"));
            this.err_trigger_already_exist = Objects.requireNonNull(configuration.getString("messages.error.trigger_already_exist"));
            this.err_trigger_not_exist = Objects.requireNonNull(configuration.getString("messages.error.trigger_not_exist"));
            this.err_name_not_contains_special = Objects.requireNonNull(configuration.getString("messages.error.name_not_contains_special"));
            this.err_item_not_set = Objects.requireNonNull(configuration.getString("messages.error.item_not_set"));
            this.err_no_empty_space = Objects.requireNonNull(configuration.getString("messages.error.no_empty_space"));
            this.err_item_already_set = Objects.requireNonNull(configuration.getString("messages.error.item_already_set"));
            this.err_invalid_type = Objects.requireNonNull(configuration.getString("messages.error.invalid_type"));
            this.err_force_is_boolean = Objects.requireNonNull(configuration.getString("messages.error.force_is_boolean"));
            this.err_command_not_set = Objects.requireNonNull(configuration.getString("messages.error.command_not_set"));
            this.err_name_already_registered = Objects.requireNonNull(configuration.getString("messages.error.name_already_registered"));
        } catch (NullPointerException e) {
            PXItemTrigger.getInstance().saveResource("messages.yml", true);
            DataManager.setup();
        }
    }

    public static Message getInstance() {
        return instance;
    }
}
