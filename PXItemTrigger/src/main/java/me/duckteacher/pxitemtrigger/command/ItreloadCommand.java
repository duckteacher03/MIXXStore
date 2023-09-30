package me.duckteacher.pxitemtrigger.command;

import me.duckteacher.pxitemtrigger.file.DataManager;
import me.duckteacher.pxitemtrigger.file.variables.message.Message;
import me.duckteacher.pxitemtrigger.file.variables.message.MessageKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItreloadCommand implements TabCompleter, CommandExecutor {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Message msgData = Message.getInstance();
        MiniMessage mm = MiniMessage.miniMessage();

        if (!sender.hasPermission("itemtrigger.command.reload")) {
            sender.sendMessage(mm.deserialize(msgData.getMessage(MessageKey.PREFIX) + " <red><msg_no_permission>",
                    Placeholder.component("msg_no_permission", mm.deserialize(msgData.getMessage(MessageKey.NO_PERMISSION)))
            ));
            return true;
        }

        DataManager.setup();
        msgData = Message.getInstance();

        sender.sendMessage(mm.deserialize(msgData.getMessage(MessageKey.PREFIX) + " <msg_config_reloaded>",
                Placeholder.component("msg_config_reloaded", mm.deserialize(msgData.getMessage(MessageKey.CONFIG_RELOADED)))
        ));

        return true;
    }
}
