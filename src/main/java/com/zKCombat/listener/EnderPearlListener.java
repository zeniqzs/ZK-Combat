package com.zKCombat.listener;

import com.zKCombat.utils.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderPearlListener implements Listener {
    private final CombatManager combatManager;
    private final JavaPlugin plugin;

    public EnderPearlListener(JavaPlugin plugin, CombatManager combatManager) {
        this.plugin = plugin;
        this.combatManager = combatManager;
    }

    @EventHandler
    public void onPearlUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.ENDER_PEARL) return;

        Player player = event.getPlayer();
        if (!combatManager.isEnderPearlAllowed() && combatManager.isInCombat(player.getUniqueId())) {
            event.setCancelled(true);
            String msg = plugin.getConfig().getString("Combat.Messages.EnderPearls");
            player.sendMessage(msg);
        }
    }
}
