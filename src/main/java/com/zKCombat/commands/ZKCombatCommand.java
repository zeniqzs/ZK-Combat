package com.zKCombat.commands;

import com.zKCombat.ZKCombat;
import com.zKCombat.utils.ColorUtil;
import com.zKCombat.utils.CombatManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ZKCombatCommand implements CommandExecutor, TabCompleter {
    private final String prefix = ColorUtil.parseColors("&7[ZKCombat] ");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(prefix + ColorUtil.parseColors("&7Usage: /zkcombat <reload|iscombat|startcombat|endcombat> [player]"));
            return true;
        }
        String sub = args[0].toLowerCase();
        ZKCombat plugin = ZKCombat.getPlugin(ZKCombat.class);
        CombatManager manager = ZKCombat.combatManager;
        boolean ignoreAdminsConfig = plugin.getConfig().getBoolean("Combat.Ignore-Admins");

        switch (sub) {
            case "reload": {
                plugin.reloadConfig();
                boolean wgEnabled = plugin.getServer().getPluginManager().getPlugin("WorldGuard") != null;
                ZKCombat.combatManager = new CombatManager(plugin, wgEnabled);
                sender.sendMessage(prefix + ColorUtil.parseColors("&aConfig reloaded."));
                break;
            }
            case "iscombat":
                if (args.length < 2) {
                    sender.sendMessage(prefix + ColorUtil.parseColors("&cUsage: /zkcombat iscombat <player>"));
                    return true;
                }
                Player target = Bukkit.getPlayerExact(args[1]);
                if (target == null) {
                    sender.sendMessage(prefix + ColorUtil.parseColors("&cPlayer not found."));
                } else {
                    boolean inCombat = manager.isInCombat(target.getUniqueId());
                    sender.sendMessage(prefix + (inCombat ? ColorUtil.parseColors("&c") : ColorUtil.parseColors("&a")) +
                            target.getName() + (inCombat ? ColorUtil.parseColors(" is in combat.") : ColorUtil.parseColors(" is not in combat.")));
                }
                break;
            default:
                sender.sendMessage(prefix + ColorUtil.parseColors("&7Unknown subcommand."));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String[] subs = {"reload", "iscombat"};
            for (String s : subs) if (s.startsWith(args[0].toLowerCase())) completions.add(s);
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("iscombat"))) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) completions.add(p.getName());
            }
        }
        return completions;
    }
}
