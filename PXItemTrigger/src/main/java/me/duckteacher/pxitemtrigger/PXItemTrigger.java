package me.duckteacher.pxitemtrigger;

import me.duckteacher.pxitemtrigger.command.ItemTriggerCommand;
import me.duckteacher.pxitemtrigger.command.ItreloadCommand;
import me.duckteacher.pxitemtrigger.file.DataManager;
import me.duckteacher.pxitemtrigger.listener.TriggerListener;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class PXItemTrigger extends JavaPlugin {
    public static PXItemTrigger instance;
    public static Logger logger;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();

        /* ---------- CONFIG ---------- */
        saveResource("messages.yml", false);
        DataManager.setup();

        /* ---------- COMMAND ---------- */
        //<editor-fold desc="Commands">
        PluginCommand itemTriggerCmd = getCommand("itemtrigger");
        Objects.requireNonNull(itemTriggerCmd).setExecutor(new ItemTriggerCommand());
        itemTriggerCmd.setTabCompleter(new ItemTriggerCommand());

        PluginCommand itreloadCmd = getCommand("itreload");
        Objects.requireNonNull(itreloadCmd).setExecutor(new ItreloadCommand());
        itreloadCmd.setTabCompleter(new ItreloadCommand());
        //</editor-fold>

        /* ---------- LISTENER ---------- */
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new TriggerListener(), this);
    }

    @Override
    public void onDisable() {
        DataManager.saveAll();
    }

    public static PXItemTrigger getInstance() {
        return instance;
    }
}
