package com.zKCombat.listener;

import com.zKCombat.utils.ColorUtil;
import com.zKCombat.utils.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ElytraListener implements Listener {
    private final CombatManager combatManager;
    private final JavaPlugin plugin;

    public ElytraListener(JavaPlugin plugin, CombatManager combatManager) {
        this.plugin = plugin;
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onElytraToggle(EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!event.isGliding()) return;

        if (!combatManager.isElytraAllowed() && combatManager.isInCombat(player.getUniqueId())) {
            event.setCancelled(true);
            String msg = plugin.getConfig().getString("Combat.Messages.Elytra");
            player.sendMessage(ColorUtil.parseMessage(msg));
        }
    }
}