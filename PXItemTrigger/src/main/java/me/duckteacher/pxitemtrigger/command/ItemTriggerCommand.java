package me.duckteacher.pxitemtrigger.command;

import me.duckteacher.pxitemtrigger.file.DataManager;
import me.duckteacher.pxitemtrigger.file.variables.Message;
import me.duckteacher.pxitemtrigger.util.trigger.Trigger;
import me.duckteacher.pxitemtrigger.util.trigger.TriggerCommand;
import me.duckteacher.pxitemtrigger.util.trigger.TriggerType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemTriggerCommand implements TabCompleter, CommandExecutor {
    private static final MiniMessage mm = MiniMessage.miniMessage();

    //<editor-fold desc="Placeholders">
    private static final Component mm_cmd = mm.deserialize("<hover:show_text:'<detail_cmd>'>/itemtrigger</hover>",
            Placeholder.component("detail_cmd", mm.deserialize(
                    "[<color:#ffdbb3>/it, /아이템트리거</color>] (으)로 교체 가능"
            )));
    private static final Component mm_cmd_create = mm.deserialize("<hover:show_text:'<detail_cmd_create>'>create</hover>",
            Placeholder.component("detail_cmd_create", mm.deserialize(
                    "[<color:#ffdbb3>생성</color>] (으)로 교체 가능"
            )));
    private static final Component mm_cmd_remove = mm.deserialize("<hover:show_text:'<detail_cmd_remove>'>remove</hover>",
            Placeholder.component("detail_cmd_remove", mm.deserialize(
                    "[<color:#ffdbb3>제거</color>] (으)로 교체 가능"
            )));
    private static final Component mm_cmd_item = mm.deserialize("<hover:show_text:'<detail_cmd_item>'>item</hover>",
            Placeholder.component("detail_cmd_item", mm.deserialize(
                    "[<color:#ffdbb3>아이템</color>] (으)로 교체 가능"
            )));
    private static final Component mm_cmd_command = mm.deserialize("<hover:show_text:'<detail_cmd_command>'>command</hover>",
            Placeholder.component("detail_cmd_command", mm.deserialize(
                    "[<color:#ffdbb3>명령어</color>] (으)로 교체 가능"
            )));
    private static final Component mm_cmd_delcmd = mm.deserialize("<hover:show_text:'<detail_cmd_delcmd>'>delcmd</hover>",
            Placeholder.component("detail_cmd_delcmd", mm.deserialize(
                    "[<color:#ffdbb3>명령어제거</color>] (으)로 교체 가능"
            )));
    private static final Component mm_cmd_rename = mm.deserialize("<hover:show_text:'<detail_cmd_rename>'>rename</hover>",
            Placeholder.component("detail_cmd_rename", mm.deserialize(
                    "[<color:#ffdbb3>이름</color>] (으)로 교체 가능"
            )));

    private static final Component mm_name = mm.deserialize("<hover:show_text:'<detail_name>'>[이름]</hover>",
            Placeholder.component("detail_name", mm.deserialize(
                    "저장될 아이템 트리거의 이름"
            )));
    private static final Component mm_type = mm.deserialize("<hover:show_text:'<detail_type>'>[타입]</hover>",
            Placeholder.component("detail_type", mm.deserialize(
                    "<red>left<reset>: 아이템을 들고 좌클릭 시 명령어 실행<br>" +
                            "<red>right<reset>: 아이템을 들고 우클릭 시 명령어 실행<br>" +
                            "<red>swap<reset>: 아이템을 들고 손 바꾸기(<key:key.swapOffhand>) 시 명령어 실행"
            )));
    private static final Component mm_force = mm.deserialize("<hover:show_text:'<detail_force>'>[강제 여부]</hover>",
            Placeholder.component("detail_force", mm.deserialize(
                    "<red>true<reset>: 일시적으로 OP의 권한을 얻어 명령을 실행<br>" +
                            "<red>false<reset>: 일반적인 상태에서 명령을 실행"
            )));
    private static final Component mm_command = mm.deserialize("<hover:show_text:'<detail_command>'>[명령어]</hover>",
            Placeholder.component("detail_command", mm.deserialize(
                    "명령어는 슬래쉬(<red>/</red>)가 하나 포함된 채로 입력됨<br>" +
                            "예시: <red>/</red>tp 명령어를 실행하고 싶다면 'tp' 입력"
            )));
    //</editor-fold>

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> comps = new ArrayList<>();
        ArrayList<String> preComps = new ArrayList<>();

        if (args.length == 1) {
            preComps.add("?");
            for (Trigger trigger : Trigger.triggers)
                preComps.add(trigger.name);
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("?"))
                preComps.addAll(List.of("1", "2"));
            else {
                String triggerName = args[0];
                if (Trigger.getTriggerByName(triggerName) == null)
                    preComps.addAll(List.of("create", "생성"));
                else
                    preComps.addAll(List.of("remove", "item", "command", "delcmd", "rename", "제거", "아이템", "명령어", "명령어제거", "이름"));
            }
        }
        else {
            String triggerName = args[0];
            Trigger trigger = Trigger.getTriggerByName(triggerName);
            if (trigger != null) {
                if (args[1].equalsIgnoreCase("command") || args[1].equals("명령어")) {
                    if (args.length == 3) {
                        for (TriggerType type : TriggerType.values())
                            preComps.add(type.name().toLowerCase());
                    }
                    else if (args.length == 4)
                        preComps.addAll(List.of("true", "false"));
                    else
                        return List.of("<명령어>");
                }
                else if (args[1].equalsIgnoreCase("delcmd") || args[1].equals("명령어제거")) {
                    if (args.length == 3) {
                        for (TriggerType type : TriggerType.values())
                            preComps.add(type.name().toLowerCase());
                    }
                }
                else if (args[1].equalsIgnoreCase("rename") || args[1].equals("이름")) {
                    if (args.length == 3)
                        return List.of("<새 이름>");
                }
            }
        }

        for (String preComp : preComps) {
            if (preComp.toLowerCase().startsWith(args[args.length-1].toLowerCase()))
                comps.add(preComp);
        }

        return comps;
    }

    private void sendHelp(Player player) { sendHelp(player, 1); }
    private void sendHelp(Player player, int page) {
        Message msgData = Message.getInstance();
        String PREFIX = msgData.prefix;
        if (page < 1 || page > 2) page = 1;

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        player.sendPlainMessage("");
        player.sendMessage(mm.deserialize(PREFIX + " 명령어 도움말 <st>━━━━━</st>[" + page + "/2]<st>━━━━━</st>"));

        switch (page) {
            case 2 -> {
                player.sendMessage(mm.deserialize(
                        "  <mm_cmd> <mm_name> <mm_cmd_command> <mm_type> <mm_force><br>" +
                                "    <gray>└ 등록된 명령어의 강제 여부를 설정합니다.</gray><br>" +
                                "  <mm_cmd> <mm_name> <mm_cmd_delcmd> <mm_type><br>" +
                                "    <gray>└ 해당 타입에 등록된 명령어를 제거합니다.</gray><br>" +
                                "  <mm_cmd> <mm_name> <mm_cmd_rename> [새 이름]<br>" +
                                "    <gray>└ 해당 아이템 트리거의 이름을 변경합니다.</gray><br>" +
                                "  /itreload<br>" +
                                "    <gray>└ 아이템 트리거 설정을 새로고침 합니다.</gray>",
                        Placeholder.component("mm_cmd", mm_cmd),
                        Placeholder.component("mm_cmd_command", mm_cmd_command),
                        Placeholder.component("mm_cmd_delcmd", mm_cmd_delcmd),
                        Placeholder.component("mm_cmd_rename", mm_cmd_rename),
                        Placeholder.component("mm_name", mm_name),
                        Placeholder.component("mm_type", mm_type),
                        Placeholder.component("mm_force", mm_force)
                ));
            }
            default ->
                    player.sendMessage(mm.deserialize(
                            "  <mm_cmd> ? [페이지(1~2)]<br>" +
                                    "    <gray>└ 아이템 트리거 명령어 도움말을 확인합니다.</gray><br>" +
                            "  <mm_cmd> <mm_name> <mm_cmd_create><br>" +
                                    "    <gray>└ 새로운 아이템 트리거를 생성합니다.</gray><br>" +
                            "  <mm_cmd> <mm_name> <mm_cmd_remove><br>" +
                                    "    <gray>└ 해당 아이템 트리거를 제거합니다.</gray><br>" +
                            "  <mm_cmd> <mm_name> <mm_cmd_item><br>" +
                                    "    <gray>└ 손에 든 아이템을 해당 아이템 트리거를 실행하는 아이템으로 설정합니다.</gray><br>" +
                                    "    <gray>└ 손에 아무것도 들고 있지 않다면 해당 아이템을 지급합니다.</gray><br>" +
                            "  <mm_cmd> <mm_name> <mm_cmd_command> <mm_type> <mm_force> <mm_command><br>" +
                                    "    <gray>└ 해당 아이템 트리거의 명령어를 설정합니다.</gray>",
                            Placeholder.component("mm_cmd", mm_cmd),
                            Placeholder.component("mm_cmd_create", mm_cmd_create),
                            Placeholder.component("mm_cmd_remove", mm_cmd_remove),
                            Placeholder.component("mm_cmd_item", mm_cmd_item),
                            Placeholder.component("mm_cmd_command", mm_cmd_command),
                            Placeholder.component("mm_name", mm_name),
                            Placeholder.component("mm_type", mm_type),
                            Placeholder.component("mm_force", mm_force),
                            Placeholder.component("mm_command", mm_command)
                    ));
        }
    }
    private String typeName(TriggerType type) {
        return switch (type) {
            case LEFT -> "왼쪽 클릭";
            case RIGHT -> "오른쪽 클릭";
            case SWAP -> "손 바꾸기";
        };
    }
    private void tinkle(Player player) {
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1f);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Message msgData = Message.getInstance();
        String PREFIX = msgData.prefix;

        if (!(sender instanceof Player player)) {
            sender.sendMessage(mm.deserialize("<red><msg_err_only_for_player>",
                    Placeholder.component("msg_err_only_for_player", mm.deserialize(msgData.err_only_for_player))
            ));
            return true;
        }

        if (args.length <= 1 || args[0].equals("?")) {
            int page = 1;

            if (args.length == 2) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_page_is_number>",
                            Placeholder.component("msg_err_page_is_number", mm.deserialize(msgData.err_page_is_number))
                    ));
                    return true;
                }
            }

            sendHelp(player, page);
            return true;
        }
        else {
            String triggerName = args[0].toLowerCase();
            Trigger trigger = Trigger.getTriggerByName(triggerName);

            if (args[1].equalsIgnoreCase("create") || args[1].equals("생성")) {
                if (args.length != 2) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red>올바른 사용법: <mm_cmd> <mm_name> <mm_cmd_create>",
                            Placeholder.component("mm_cmd", mm_cmd),
                            Placeholder.component("mm_name", mm_name),
                            Placeholder.component("mm_cmd_create", mm_cmd_create))
                    );
                    return true;
                }

                if (trigger != null) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_trigger_already_exist>",
                            Placeholder.component("msg_err_trigger_already_exist", mm.deserialize(msgData.err_trigger_already_exist))
                    ));
                    return true;
                }

                Pattern pattern = Pattern.compile("[^a-z0-9_]");
                Matcher matcher = pattern.matcher(triggerName);
                if (matcher.find()) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_name_not_contains_special>",
                            Placeholder.component("msg_err_name_not_contains_special", mm.deserialize(msgData.err_name_not_contains_special))
                    ));
                    return true;
                }

                Trigger newTrigger = new Trigger(triggerName);
                Trigger.triggers.add(newTrigger);

                DataManager.saveAll();
                tinkle(player);

                String msg = msgData.trigger_created.replace("<trigger_name>", triggerName);
                player.sendMessage(mm.deserialize(PREFIX + " <msg_trigger_created>",
                        Placeholder.component("msg_trigger_created", mm.deserialize(msg))
                ));
                return true;
            }

            else if (args[1].equalsIgnoreCase("remove") || args[1].equals("제거")) {
                if (args.length != 2) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red>올바른 사용법: <mm_cmd> <mm_name> <mm_cmd_remove>",
                            Placeholder.component("mm_cmd", mm_cmd),
                            Placeholder.component("mm_name", mm_name),
                            Placeholder.component("mm_cmd_remove", mm_cmd_remove))
                    );
                    return true;
                }

                if (trigger == null) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_trigger_not_exist>",
                            Placeholder.component("msg_err_trigger_not_exist", mm.deserialize(msgData.err_trigger_not_exist))
                    ));
                    return true;
                }

                Trigger.triggers.remove(trigger);

                DataManager.saveAll();
                tinkle(player);

                String msg = msgData.trigger_removed.replace("<trigger_name>", triggerName);
                player.sendMessage(mm.deserialize(PREFIX + " <msg_trigger_removed>",
                        Placeholder.component("msg_trigger_removed", mm.deserialize(msg))
                ));
                return true;
            }

            else if (args[1].equalsIgnoreCase("item") || args[1].equals("아이템")) {
                if (args.length != 2) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red>올바른 사용법: <mm_cmd> <mm_name> <mm_cmd_item>",
                            Placeholder.component("mm_cmd", mm_cmd),
                            Placeholder.component("mm_name", mm_name),
                            Placeholder.component("mm_cmd_item", mm_cmd_item))
                    );
                    return true;
                }

                if (trigger == null) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_trigger_not_exist>",
                            Placeholder.component("msg_err_trigger_not_exist", mm.deserialize(msgData.err_trigger_not_exist))
                    ));
                    return true;
                }

                ItemStack item = player.getInventory().getItemInMainHand();
                item = item.clone();
                item.setAmount(1);

                if (item.getType().isAir()) {   // empty
                    ItemStack nowItem = trigger.item;
                    if (nowItem == null) {
                        player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_item_not_set>",
                                Placeholder.component("msg_err_item_not_set", mm.deserialize(msgData.err_item_not_set))
                        ));
                        return true;
                    }
                    else {
                        if (player.getInventory().firstEmpty() == -1) {
                            player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_no_empty_space>",
                                    Placeholder.component("msg_err_no_empty_space", mm.deserialize(msgData.err_no_empty_space))
                            ));
                            return true;
                        }

                        player.getInventory().addItem(nowItem.clone());

                        DataManager.saveAll();
                        tinkle(player);

                        String msg = msgData.received_item.replace("<trigger_name>", triggerName);
                        player.sendMessage(mm.deserialize(PREFIX + " <msg_received_item>",
                                Placeholder.component("msg_received_item", mm.deserialize(msg))
                        ));
                        return true;
                    }
                }
                else {  // not empty
                    if (Trigger.getTrigger(item) != null) {
                        player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_item_already_set>",
                                Placeholder.component("msg_err_item_already_set", mm.deserialize(msgData.err_item_already_set))
                        ));
                        return true;
                    }

                    trigger.item = item;

                    DataManager.saveAll();
                    tinkle(player);

                    String msg = msgData.item_set.replace("<trigger_name>", triggerName);
                    player.sendMessage(mm.deserialize(PREFIX + " <msg_item_set>",
                            Placeholder.component("msg_item_set", mm.deserialize(msg))
                    ));
                    return true;
                }
            }

            else if (args[1].equalsIgnoreCase("command") || args[1].equals("명령어")) {
                if (args.length < 4) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red>올바른 사용법: <mm_cmd> <mm_name> <mm_cmd_command> <mm_type> <mm_force> <mm_command>",
                            Placeholder.component("mm_cmd", mm_cmd),
                            Placeholder.component("mm_name", mm_name),
                            Placeholder.component("mm_cmd_command", mm_cmd_command),
                            Placeholder.component("mm_type", mm_type),
                            Placeholder.component("mm_force", mm_force),
                            Placeholder.component("mm_command", mm_command))
                    );
                    return true;
                }

                if (trigger == null) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_trigger_not_exist>",
                            Placeholder.component("msg_err_trigger_not_exist", mm.deserialize(msgData.err_trigger_not_exist))
                    ));
                    return true;
                }

                if (args.length == 4) {
                    String typeStr = args[2].toUpperCase();
                    String forceStr = args[3];

                    TriggerType type;
                    String typeName;
                    try {
                        type = TriggerType.valueOf(typeStr);
                        typeName = typeName(type);
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_invalid_type>",
                                Placeholder.component("msg_err_invalid_type", mm.deserialize(msgData.err_invalid_type))
                        ));
                        return true;
                    }

                    boolean force;
                    if (forceStr.equalsIgnoreCase("true")) force = true;
                    else if (forceStr.equalsIgnoreCase("false")) force = false;
                    else {
                        player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_force_is_boolean>",
                                Placeholder.component("msg_err_force_is_boolean", mm.deserialize(msgData.err_force_is_boolean))
                        ));
                        return true;
                    }

                    TriggerCommand triggerCommand = trigger.getCommand(type);
                    if (triggerCommand == null) {
                        player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_command_not_set>",
                                Placeholder.component("msg_err_command_not_set", mm.deserialize(msgData.err_command_not_set))
                        ));
                        return true;
                    }

                    triggerCommand.force = force;

                    DataManager.saveAll();
                    tinkle(player);

                    String msg = msgData.force_set
                            .replace("<trigger_name>", triggerName)
                            .replace("<type_name>", typeName)
                            .replace("<force>", Boolean.toString(force));
                    player.sendMessage(mm.deserialize(PREFIX + " <msg_force_set>",
                            Placeholder.component("msg_force_set", mm.deserialize(msg))
                    ));
                    return true;
                }
                else {
                    String typeStr = args[2].toUpperCase();
                    String forceStr = args[3];
                    String commandline = String.join(" ", Arrays.copyOfRange(args, 4, args.length));

                    TriggerType type;
                    String typeName;
                    try {
                        type = TriggerType.valueOf(typeStr);
                        typeName = typeName(type);
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_invalid_type>",
                                Placeholder.component("msg_err_invalid_type", mm.deserialize(msgData.err_invalid_type))
                        ));
                        return true;
                    }

                    boolean force;
                    if (forceStr.equalsIgnoreCase("true")) force = true;
                    else if (forceStr.equalsIgnoreCase("false")) force = false;
                    else {
                        player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_force_is_boolean>",
                                Placeholder.component("msg_err_force_is_boolean", mm.deserialize(msgData.err_force_is_boolean))
                        ));
                        return true;
                    }

                    TriggerCommand triggerCommand = new TriggerCommand(commandline, force);
                    trigger.setCommand(type, triggerCommand);

                    DataManager.saveAll();
                    tinkle(player);

                    String msg = msgData.command_set
                            .replace("<trigger_name>", triggerName)
                            .replace("<type_name>", typeName);
                    player.sendMessage(mm.deserialize(PREFIX + " <msg_command_set>",
                            Placeholder.component("msg_command_set", mm.deserialize(msg))
                    ));
                    return true;
                }
            }

            else if (args[1].equalsIgnoreCase("delcmd") || args[1].equals("명령어제거")) {
                if (args.length != 3) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red>올바른 사용법: <mm_cmd> <mm_name> <mm_cmd_delcmd> <mm_type>",
                            Placeholder.component("mm_cmd", mm_cmd),
                            Placeholder.component("mm_name", mm_name),
                            Placeholder.component("mm_cmd_delcmd", mm_cmd_delcmd),
                            Placeholder.component("mm_type", mm_type))
                    );
                    return true;
                }

                if (trigger == null) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_trigger_not_exist>",
                            Placeholder.component("msg_err_trigger_not_exist", mm.deserialize(msgData.err_trigger_not_exist))
                    ));
                    return true;
                }

                String typeStr = args[2].toUpperCase();
                TriggerType type;
                String typeName;
                try {
                    type = TriggerType.valueOf(typeStr);
                    typeName = typeName(type);
                } catch (IllegalArgumentException e) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_invalid_type>",
                            Placeholder.component("msg_err_invalid_type", mm.deserialize(msgData.err_invalid_type))
                    ));
                    return true;
                }

                if (trigger.getCommand(type) == null) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_command_not_set>",
                            Placeholder.component("msg_err_command_not_set", mm.deserialize(msgData.err_command_not_set))
                    ));
                    return true;
                }

                trigger.setCommand(type, null);

                DataManager.saveAll();
                tinkle(player);

                String msg = msgData.command_removed
                        .replace("<trigger_name>", triggerName)
                        .replace("<type_name>", typeName);
                player.sendMessage(mm.deserialize(PREFIX + " <msg_command_removed>",
                        Placeholder.component("msg_command_removed", mm.deserialize(msg))
                ));
                return true;
            }

            else if (args[1].equalsIgnoreCase("rename") || args[1].equals("이름")) {
                if (args.length != 3) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red>올바른 사용법: <mm_cmd> <mm_name> <mm_cmd_rename> [새 이름]",
                            Placeholder.component("mm_cmd", mm_cmd),
                            Placeholder.component("mm_name", mm_name),
                            Placeholder.component("mm_cmd_rename", mm_cmd_rename))
                    );
                    return true;
                }

                if (trigger == null) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_trigger_not_exist>",
                            Placeholder.component("msg_err_trigger_not_exist", mm.deserialize(msgData.err_trigger_not_exist))));
                    return true;
                }

                String newName = args[2].toLowerCase();
                if (Trigger.getTriggerByName(newName) != null) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_name_already_registered>",
                            Placeholder.component("msg_err_name_already_registered", mm.deserialize(msgData.err_name_already_registered))
                    ));
                    return true;
                }

                Pattern pattern = Pattern.compile("[^a-z0-9_]");
                Matcher matcher = pattern.matcher(newName);
                if (matcher.find()) {
                    player.sendMessage(mm.deserialize(PREFIX + " <red><msg_err_name_not_contains_special>",
                            Placeholder.component("msg_err_name_not_contains_special", mm.deserialize(msgData.err_name_not_contains_special))
                    ));
                    return true;
                }

                trigger.name = newName;

                DataManager.saveAll();
                tinkle(player);

                String msg = msgData.name_changed
                        .replace("<trigger_name>", triggerName)
                        .replace("<new_name>", newName);
                player.sendMessage(mm.deserialize(PREFIX + " <msg_name_changed>",
                        Placeholder.component("msg_name_changed", mm.deserialize(msg))
                ));
                return true;
            }

            else {
                sendHelp(player);
                return true;
            }
        }
    }
}
