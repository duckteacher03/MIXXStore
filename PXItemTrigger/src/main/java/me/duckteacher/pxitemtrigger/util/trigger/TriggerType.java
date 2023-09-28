package me.duckteacher.pxitemtrigger.util.trigger;

public enum TriggerType {
    LEFT, RIGHT, SWAP;

    public boolean equals(TriggerType type) {
        return (this == type);
    }
}
