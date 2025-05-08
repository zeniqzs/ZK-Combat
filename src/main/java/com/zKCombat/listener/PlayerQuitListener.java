package com.zKCombat.listener;

import com.zKCombat.utils.ColorUtil;
import com.zKCombat.utils.CombatManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerQuitListener implements Listener {

    private final JavaPlugin plugin;
    private final CombatManager combatManager;

    public PlayerQuitListener(JavaPlugin plugin, CombatManager combatManager) {
        this.plugin = plugin;
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (combatManager.isInCombat(player.getUniqueId())) {
            player.setHealth(0);

            combatManager.endCombat(player);

            combatManager.endSoloCombat();
            boolean allow = plugin.getConfig().getBoolean("Combat.Allow-Broadcast-Messages");
            if (allow && plugin.getServer().getPluginManager().getPermission("zkcombat.broadcast") != null) {
                String msg = plugin.getConfig()
                        .getString("Combat.Messages.Left-Broadcast")
                        .replace("%player%", player.getName());
                Bukkit.broadcastMessage(ColorUtil.parseMessage(msg));
            }
        }
    }
}
