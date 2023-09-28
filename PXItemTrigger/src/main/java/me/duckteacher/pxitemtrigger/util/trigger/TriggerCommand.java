package me.duckteacher.pxitemtrigger.util.trigger;

public class TriggerCommand {
    public String command;
    public boolean force;

    public TriggerCommand(String command, boolean force) {
        this.command = command;
        this.force = force;
    }
}
