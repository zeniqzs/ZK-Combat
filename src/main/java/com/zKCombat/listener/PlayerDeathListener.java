package com.zKCombat.listener;

import com.zKCombat.ZKCombat;
import com.zKCombat.utils.CombatManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;


public class PlayerDeathListener implements Listener {

    CombatManager combatManager = new CombatManager(ZKCombat.getProvidingPlugin(ZKCombat.class), false);

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (combatManager.isInCombat(player.getUniqueId())) {
            combatManager.endCombat(player);
            combatManager.endSoloCombat();
        }
    }

}