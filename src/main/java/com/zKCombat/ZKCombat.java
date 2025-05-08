package com.zKCombat;

import com.zKCombat.commands.ZKCombatCommand;
import com.zKCombat.listener.*;
import com.zKCombat.utils.CombatManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ZKCombat extends JavaPlugin {
    public static CombatManager combatManager;
    private boolean worldGuardEnabled;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        worldGuardEnabled = getServer().getPluginManager().getPlugin("WorldGuard") != null;
        if (!worldGuardEnabled) {
            getLogger().warning("[ZKCombat] WorldGuard not found");
        }

        combatManager = new CombatManager(this, worldGuardEnabled);

        getServer().getPluginManager().registerEvents(new PlayerAttackListener(combatManager), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this, combatManager), this);
        getServer().getPluginManager().registerEvents(new PlayerChatListener(this, combatManager), this);
        getServer().getPluginManager().registerEvents(new ElytraListener(this, combatManager), this);
        getServer().getPluginManager().registerEvents(new EnderPearlListener(this, combatManager), this);

        getServer().getPluginCommand("zkcombat").setExecutor(new ZKCombatCommand());
        getServer().getPluginCommand("zkcombat").setTabCompleter(new ZKCombatCommand());
    }

}
