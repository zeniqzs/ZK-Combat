package com.zKCombat.utils;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CombatManager {
    private final JavaPlugin plugin;
    private final int duration;
    private final List<String> ignoredWorlds;
    private final List<String> ignoredZones;
    private final boolean ignoreAdmins;
    private final boolean allowElytra;
    private final boolean allowEnderPearls;
    private final String actionBarMessage;

    private final boolean worldGuardEnabled;
    private final RegionQuery regionQuery;

    private final Map<UUID, Integer> timers = new ConcurrentHashMap<>();
    private final Map<UUID, BukkitTask> tasks = new ConcurrentHashMap<>();

    public CombatManager(JavaPlugin plugin, boolean wgEnabled) {
        this.plugin = plugin;
        this.worldGuardEnabled = wgEnabled;

        duration = plugin.getConfig().getInt("Combat.Duration");
        ignoredWorlds = plugin.getConfig().getStringList("Combat.Ignored-Worlds");
        ignoredZones = plugin.getConfig().getStringList("Combat.Ignored-Zones");
        ignoreAdmins = plugin.getConfig().getBoolean("Combat.Ignore-Admins");
        allowElytra = plugin.getConfig().getBoolean("Combat.Allow-Elytra");
        allowEnderPearls = plugin.getConfig().getBoolean("Combat.Allow-EnderPearls");
        actionBarMessage = plugin.getConfig().getString("Combat.Action-Bar");

        if (worldGuardEnabled) {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            regionQuery = container.createQuery();
        } else {
            regionQuery = null;
        }
    }

    public void startCombat(Player player) {
        if (isIgnoredWorld(player.getWorld().getName())) return;
        if (isIgnoredZone(player)) return;
        if (ignoreAdmins && (player.isOp() || player.hasPermission("zkcombat.ignore"))) return;

        UUID id = player.getUniqueId();
        timers.put(id, duration);
        if (tasks.containsKey(id)) tasks.get(id).cancel();

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            int timeLeft = timers.getOrDefault(id, 0);
            if (timeLeft <= 0) {
                endCombat(player);
            } else {
                String msg = actionBarMessage.replace("%seconds%", String.valueOf(timeLeft));
                player.spigot().sendMessage(
                        net.md_5.bungee.api.ChatMessageType.ACTION_BAR,
                        new net.md_5.bungee.api.chat.TextComponent(ColorUtil.parseColors(msg))
                );
                timers.put(id, timeLeft - 1);
            }
        }, 0L, 20L);

        tasks.put(id, task);
    }

    public void endCombat(Player player) {
        UUID id = player.getUniqueId();
        timers.remove(id);
        if (tasks.containsKey(id)) {
            tasks.get(id).cancel();
            tasks.remove(id);
        }
    }

    public boolean isInCombat(UUID id) {
        return timers.containsKey(id);
    }

    public boolean isElytraAllowed() {
        return allowElytra;
    }

    public boolean isEnderPearlAllowed() {
        return allowEnderPearls;
    }

    private boolean isIgnoredWorld(String worldName) {
        return ignoredWorlds.contains(worldName);
    }

    private boolean isIgnoredZone(Player player) {
        if (!worldGuardEnabled || regionQuery == null) return false;
        try {
            BukkitWorld wgWorld = (BukkitWorld) BukkitAdapter.adapt(player.getWorld());
            com.sk89q.worldedit.util.Location loc = BukkitAdapter.adapt(player.getLocation());
            Set<ProtectedRegion> regions = regionQuery.getApplicableRegions(loc).getRegions();
            for (ProtectedRegion region : regions) {
                if (ignoredZones.contains(region.getId())) {
                    return true;
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    public void endSoloCombat() {
        if (timers.size() == 1) {
            UUID solo = timers.keySet().iterator().next();
            Player p = Bukkit.getPlayer(solo);
            if (p != null && p.isOnline()) {
                endCombat(p);
            }
        }
    }
}
