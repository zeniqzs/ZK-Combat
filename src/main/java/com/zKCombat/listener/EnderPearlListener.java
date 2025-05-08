package com.zKCombat.listener;

import com.zKCombat.utils.ColorUtil;
import com.zKCombat.utils.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EnderPearlListener implements Listener {
    private final CombatManager combatManager;
    private final JavaPlugin plugin;
    private final Map<UUID, Long> lastMessageTime = new HashMap<>();

    private static final long MESSAGE_COOLDOWN_MS = 500; // 0.5 seconnds

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
        UUID playerId = player.getUniqueId();

        if (!combatManager.isEnderPearlAllowed() && combatManager.isInCombat(playerId)) {
            event.setCancelled(true);

            long now = System.currentTimeMillis();
            long lastSent = lastMessageTime.getOrDefault(playerId, 0L);

            if (now - lastSent >= MESSAGE_COOLDOWN_MS) {
                String msg = plugin.getConfig().getString("Combat.Messages.EnderPearls");
                player.sendMessage(ColorUtil.parseMessage(msg));
                lastMessageTime.put(playerId, now);
            }
        }
    }
}
