package me.duckteacher.pxitemtrigger.command;

import me.duckteacher.pxitemtrigger.file.DataManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
        DataManager.load();
        sender.sendMessage(MiniMessage.miniMessage().deserialize("아이템 트리거 설정이 모두 새로고침 되었습니다."));

        return true;
    }
}
