package me.duckteacher.pxitemtrigger.listener;

import me.duckteacher.pxitemtrigger.util.trigger.Trigger;
import me.duckteacher.pxitemtrigger.util.trigger.TriggerCommand;
import me.duckteacher.pxitemtrigger.util.trigger.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class TriggerListener implements Listener {
    private static void performCommand(TriggerCommand triggerCommand, CommandSender sender) {
        String command = triggerCommand.command;
        if (command == null)
            return;

        if (triggerCommand.force) {
            if (!sender.isOp()) {
                sender.setOp(true);
                Bukkit.dispatchCommand(sender, command);
                sender.setOp(false);
            } else Bukkit.dispatchCommand(sender, command);
        } else Bukkit.dispatchCommand(sender, command);
    }

    @EventHandler
    public static void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getHand() == null || e.getHand() == EquipmentSlot.OFF_HAND)
            return;

        ItemStack item = e.getItem();
        if (item == null || item.getType().isAir())
            return;

        item = item.clone();
        item.setAmount(1);

        Trigger trigger = Trigger.getTrigger(item);
        if (trigger == null)
            return;

        Action action = e.getAction();
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            TriggerCommand triggerCommand = trigger.getCommand(TriggerType.LEFT);
            if (triggerCommand != null) {
                e.setCancelled(true);
                performCommand(triggerCommand, player);
            }
        }
        else if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
            TriggerCommand triggerCommand = trigger.getCommand(TriggerType.RIGHT);
            if (triggerCommand != null) {
                e.setCancelled(true);
                performCommand(triggerCommand, player);
            }
        }
    }

    @EventHandler
    public static void onSwap(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();

        ItemStack item = e.getOffHandItem();
        if (item == null || item.getType().isAir())
            return;

        item = item.clone();
        item.setAmount(1);

        Trigger trigger = Trigger.getTrigger(item);
        if (trigger == null)
            return;

        TriggerCommand triggerCommand = trigger.getCommand(TriggerType.SWAP);
        if (triggerCommand != null) {
            e.setCancelled(true);
            performCommand(triggerCommand, player);
        }
    }
}
