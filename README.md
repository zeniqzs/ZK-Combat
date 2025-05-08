# ZK-Combat

[![Minecraft Version](https://img.shields.io/badge/Minecraft-1.18+-blue)](https://www.spigotmc.org/)
[![License](https://img.shields.io/badge/License-MIT-green)](#license)

A simple, configurable combat tagging plugin for Spigot/Bukkit servers that prevents players from logging out mid-combat, disables certain actions while in combat, and integrates with WorldGuard regions.

## 📦 Features

* **Combat Tagging**: Players are tagged for combat when they attack or are attacked.
* **Combat Duration**: Configurable timer (default: 15 seconds).
* **Logout Prevention**: Broadcast and kill players who disconnect while in combat.
* **Action Bar Indicator**: Shows remaining seconds in combat.
* **Blocked Actions**: Disable Elytra, ender pearls, or commands during combat.
* **WorldGuard Integration**: Ignore combat tagging in specified regions.
* **Permission-based Admins**: Option to exempt OPs or specific permissions.

## ⚙️ Configuration

Located in `plugins/ZKCombat/config.yml` (auto-generated on first run):

```yaml
Combat:
  Action-Bar: "&7You are in Combat → %seconds% seconds"
  Duration: 15                  # Combat tag duration in seconds
  Ignore-Admins: false          # Exempt OPs or permission `zkcombat.ignore`
  Allow-Elytra: true            # Allow Elytra while in combat
  Allow-EnderPearls: true       # Allow Ender Pearls while in combat

  Messages:
    Left-Broadcast: "&7%player% left the Server while Combat and died."
    Left: "&7You killed %player% because they left while Combat."
    Combat-Broadcast: "&7%player% is in Combat with %victim%"
    Combat: "&7You are now in Combat with %player%"
    Command: "&7You can't use %command% in Combat."
    Elytra: "&7You can't use Elytra in Combat."
    EnderPearls: "&7You can't use Ender Pearls in Combat."

  Allow-Broadcast-Messages: true  # Disable all broadcast messages if false

  Blocked-Commands:
    - shop
    - tpa
    - tpaccept

  Ignored-Zones:                  # WorldGuard region IDs to ignore
    - zone1
    - zone2

  Ignored-Worlds:                 # Worlds to ignore combat tagging
    - spawn
    - farm
```

## 🚀 Installation

1. Download the latest JAR from the [releases page](https://github.com/zeniqzs/ZK-Combat/releases).
2. Place `ZKCombat.jar` into your server's `/plugins` folder.
3. Restart or reload your server.
4. Adjust settings in `plugins/ZKCombat/config.yml` as needed.

## 🚩 Commands

| Command     | Permission       | Description                       |
| ----------- | ---------------- | --------------------------------- |
| `/zkcombat` | `zkcombat.admin` | Opens the admin command interface |

## 🔑 Permissions

| Permission           | Default | Description                                 |
| -------------------- | ------- | ------------------------------------------- |
| `zkcombat.ignore`    | `false` | Exempt from combat tagging (OP or explicit) |
| `zkcombat.broadcast` | `true`  | Receive combat logout broadcast messages    |

## 🔗 Dependencies

* [WorldGuard](https://enginehub.org/worldguard/) (optional)

If WorldGuard is not found, region-based ignoring will be disabled.

## 🛠️ Development & Building

This plugin is built with Maven/JavaPlugin for Spigot 1.18.

To build locally:

```bash
mvn clean package
# Then copy target/ZKCombat-1.0-SNAPSHOT.jar to your plugins folder.
```

## 📄 License

If you are forking the Plugin, please give me Credits i would be very thankfull.
