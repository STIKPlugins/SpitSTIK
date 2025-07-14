# SpitSTIK - Highly Configurable Spit Plugin

**SpitSTIK** is a customizable Minecraft plugin that adds `/spit` command, allowing players to launch spit projectiles with adjustable mechanics.

**Join our** [**Discord**](https://discord.gg/YGzA4UxzFB) — discover other cool plugins, suggest new features, or get help with configuration.
### Features ✨

- **Customizable Spit Mechanics**:
  - Adjust velocity, cooldown, damage, and sound effects
  - Multiple permission-based groups with overriding settings
  - CoreProtect integration for item frame logging
  - MiniMessage support for rich chat formatting

* **Projectile Control**:
  - Toggleable damage (`defaultDoDamage`)
  - Spit action is accompanied by ENTITY_LLAMA_SPIT sound
  - Fine-tuned audio controls (volume/pitch)

- **Group System**:
  - Priority-based permissions (`group1`-`group3` extendable)
  - Unique settings per group (velocity up to 1.5 default)
  - Cooldowns from 4-10 seconds

* **Safety & Logging**:
  - Configurable **CoreProtect logging** for item frame interactions
  - Player-specific cooldown tracking
  - Permission-locked commands

### Main config (`config.yml`) ⚙️

<details>
  <summary><strong>config.yml</strong></summary>

  ```yaml
# Join our discord - https://discord.gg/YGzA4UxzFB you can find another good plugins there.
# Permissions
# "SpitSTIK.use" - permission required to use "/spit" command
# "SpitSTIK.reload" - permission required to use "/spit reload" command

# default cooldown in milliseconds for "/spit" command
defaultCooldown: 10000

# default velocity with which launch spit
defaultVelocity: 0.4

# Plays the "ENTITY_LLAMA_SPIT" sound when the "/spit" command is used.
defaultUseSound: true

# does damage from "/spit"
defaultDoDamage: false

# default Volume
defaultVolume: 1.0

# default Pitch
defaultPitch: 1.0

# Material - item in main hand
# is material required to use "/spit", if materialsList is empty, material is not required
defaultMaterialsList:
#  - "WHITE_DYE"

# Log to CoreProtect when a spit projectile removes an item from an ItemFrame.
logCoreProtect: false
coreProtectPrefix: "#SpitSTIK-"

# special groups which have their own cooldown, velocity, useSound and doDamage to "/spit"
# if you don't use special groups, "/spit" will be launched with default cooldown, velocity, useSound, doDamage, volume, pitch
useSpecialGroups: false

# you have 3 groups here, but you create another ones, isn't it great?
# you just need to create another group, but you need to do this inside "groups:" block
# All group names must follow the format "groupX", where X is a number (e.g., "group4", "group23").
# and of course all groups must have cooldown, velocity, useSound, doDamage, volume, pitch
# If a player has more than one group, the one with the highest number will have priority.
groups:
  # permission required - "SpitSTIK.group1"
  group1:
    cooldown: 8000
    velocity: 0.7
    useSound: true
    doDamage: false
    volume: 0.7
    pitch: 1.0
    materialsList:
#      - "LIGHT_GRAY_DYE"

  # permission required - "SpitSTIK.group2"
  group2:
    cooldown: 6000
    velocity: 1.0
    useSound: true
    doDamage: false
    volume: 1.0
    pitch: 1.0
    materialsList:

  # permission required - "SpitSTIK.group3"
  group3:
    cooldown: 4000
    velocity: 1.5
    useSound: true
    doDamage: false
    volume: 3.0
    pitch: 0.5
    materialsList:
#      - "DARK_GRAY_DYE"
  ```
</details>

### Messages Config (`messages.yml`)
<details>
  <summary><strong>messages.yml</strong></summary>

  ```yaml
# this config supports MiniMessage
configReloaded: "<green>✔ <white>Config has been reloaded"

listener:
  spitReceive: "Player %player% spat on you"

command:
  onlyPlayerCanUse: "<red>✘ <white>This command can only be used by a player"
  noPermissionToUse: "<red>✘ <white>You do not have permission to use this command"
  wrongMaterial: "<red>✘ <white>You need special item in hand to use this command"
  noPermissionToReload: "<red>✘ <white>You do not have permission to reload the SpitSTIK plugin"
  cooldownRemaining: "<red>✘ <white>Please wait <cooldownRemaining>s."
  ```
</details>

### Permissions 🔐
| Permission Node          | Description                     |
|--------------------------|---------------------------------|
| `SpitSTIK.use`           | Basic spit command access       |
| `SpitSTIK.reload`        | Config reload permission        |
| `SpitSTIK.group[1-X]`    | Tiered group system access      |

### Commands 📟
| Command                  | Description                     |
|--------------------------|---------------------------------|
| `/spit`                  | Launch projectile               |
| `/spit reload`           | Reload configs                  |
