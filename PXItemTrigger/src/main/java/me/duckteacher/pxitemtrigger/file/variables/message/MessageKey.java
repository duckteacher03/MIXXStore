package me.duckteacher.pxitemtrigger.file.variables.message;

public enum MessageKey {
    PREFIX("normal.prefix"),
    CONFIG_RELOADED("normal.config_reloaded"),
    NO_PERMISSION("normal.no_permission"),

    TRIGGER_INFO("main.trigger_info"),
    TRIGGER_CREATED("main.trigger_created"),
    TRIGGER_REMOVED("main.trigger_removed"),
    RECEIVED_ITEM("main.received_item"),
    ITEM_SET("main.item_set"),
    FORCE_SET("main.force_set"),
    COMMAND_SET("main.command_set"),
    COMMAND_REMOVED("main.command_removed"),
    NAME_CHANGED("main.name_changed"),

    ERR_ONLY_FOR_PLAYER("error.only_for_player"),
    ERR_PAGE_IS_NUMBER("error.page_is_number"),
    ERR_TRIGGER_ALREADY_EXIST("error.trigger_already_exist"),
    ERR_TRIGGER_NOT_EXIST("error.trigger_not_exist"),
    ERR_NAME_NOT_CONTAINS_SPECIAL("error.name_not_contains_special"),
    ERR_ITEM_NOT_SET("error.item_not_set"),
    ERR_NO_EMPTY_SPACE("error.no_empty_space"),
    ERR_ITEM_ALREADY_SET("error.item_already_set"),
    ERR_INVALID_TYPE("error.invalid_type"),
    ERR_FORCE_IS_BOOLEAN("error.force_is_boolean"),
    ERR_COMMAND_NOT_SET("error.command_not_set"),
    ERR_NAME_ALREADY_REGISTERED("error.name_already_registered");

    private final String key;

    MessageKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
