package com.zKCombat.listener;

import com.zKCombat.utils.ColorUtil;
import com.zKCombat.utils.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PlayerChatListener implements Listener {
    private final CombatManager combatManager;
    private final JavaPlugin plugin;
    private final List<String> blocked;

    public PlayerChatListener(JavaPlugin plugin, CombatManager combatManager) {
        this.plugin = plugin;
        this.combatManager = combatManager;
        blocked = plugin.getConfig().getStringList("Combat.Blocked-Commands");
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (combatManager.isInCombat(player.getUniqueId())) {
            String message = event.getMessage().substring(1).split(" ")[0].toLowerCase();
            if (blocked.contains(message)) {
                String msg = plugin.getConfig().getString("Combat.Messages.Command")
                        .replace("%command%", message);
                player.sendMessage(ColorUtil.parseMessage(msg));
                event.setCancelled(true);
            }
        }
    }
}
