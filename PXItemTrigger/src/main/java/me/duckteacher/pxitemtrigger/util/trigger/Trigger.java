package me.duckteacher.pxitemtrigger.util.trigger;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

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
