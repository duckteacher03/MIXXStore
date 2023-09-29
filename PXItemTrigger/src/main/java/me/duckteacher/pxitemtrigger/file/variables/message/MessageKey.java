package me.duckteacher.pxitemtrigger.file.variables.message;

public enum MessageKey {
    PREFIX("prefix"),
    TRIGGER_CREATED("messages.trigger_created"),
    TRIGGER_REMOVED("messages.trigger_removed"),
    RECEIVED_ITEM("messages.received_item"),
    ITEM_SET("messages.item_set"),
    FORCE_SET("messages.force_set"),
    COMMAND_SET("messages.command_set"),
    COMMAND_REMOVED("messages.command_removed"),
    NAME_CHANGED("messages.name_changed"),
    CONFIG_RELOADED("messages.config_reloaded"),

    ERR_ONLY_FOR_PLAYER("messages.error.only_for_player"),
    ERR_PAGE_IS_NUMBER("messages.error.page_is_number"),
    ERR_TRIGGER_ALREADY_EXIST("messages.error.trigger_already_exist"),
    ERR_TRIGGER_NOT_EXIST("messages.error.trigger_not_exist"),
    ERR_NAME_NOT_CONTAINS_SPECIAL("messages.error.name_not_contains_special"),
    ERR_ITEM_NOT_SET("messages.error.item_not_set"),
    ERR_NO_EMPTY_SPACE("messages.error.no_empty_space"),
    ERR_ITEM_ALREADY_SET("messages.error.item_already_set"),
    ERR_INVALID_TYPE("messages.error.invalid_type"),
    ERR_FORCE_IS_BOOLEAN("messages.error.force_is_boolean"),
    ERR_COMMAND_NOT_SET("messages.error.command_not_set"),
    ERR_NAME_ALREADY_REGISTERED("messages.error.name_already_registered");

    private final String key;

    MessageKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
