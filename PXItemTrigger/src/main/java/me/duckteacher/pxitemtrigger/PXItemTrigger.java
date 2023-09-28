package me.duckteacher.pxitemtrigger;

import me.duckteacher.pxitemtrigger.command.ItemTriggerCommand;
import me.duckteacher.pxitemtrigger.command.ItreloadCommand;
import me.duckteacher.pxitemtrigger.file.DataManager;
import me.duckteacher.pxitemtrigger.listener.TriggerListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class PXItemTrigger extends JavaPlugin {
    public static PXItemTrigger instance;
    public static Logger logger;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new TriggerListener(), this);

        //<editor-fold desc="Commands">
        PluginCommand var = getCommand("itemtrigger");
        if (var != null) {
            var.setTabCompleter(new ItemTriggerCommand());
            var.setExecutor(new ItemTriggerCommand());
        }
        var = getCommand("itreload");
        if (var != null) {
            var.setTabCompleter(new ItreloadCommand());
            var.setExecutor(new ItreloadCommand());
        }
        //</editor-fold>

        DataManager.load();
    }

    @Override
    public void onDisable() {
        DataManager.save();
    }

    public static PXItemTrigger getInstance() {
        return instance;
    }
}
