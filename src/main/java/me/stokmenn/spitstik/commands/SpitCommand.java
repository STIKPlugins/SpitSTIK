package me.stokmenn.spitstik.commands;

import me.stokmenn.spitstik.GroupData;
import me.stokmenn.spitstik.config.Config;
import me.stokmenn.spitstik.config.Messages;
import net.coreprotect.CoreProtect;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.stokmenn.spitstik.SpitSTIK.coreProtectAPI;

public class SpitCommand implements CommandExecutor {

    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();

    private final JavaPlugin plugin;
    private final Messages messages;

    public SpitCommand(JavaPlugin plugin, Messages messages) {
        this.plugin = plugin;
        this.messages = messages;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1 && args[0].equals("reload")) {
            reloadConfig(sender);
            return true;
        }
        if (!(sender instanceof Player player)) {
            sender.sendMessage(messages.get("command.onlyPlayerCanUse"));
            return true;
        } else if (!player.hasPermission("SpitSTIK.use")) {
            player.sendMessage(messages.get("command.noPermissionToUse"));
            return true;
        }

        long currentTime = System.currentTimeMillis();
        long lastUsed = cooldowns.getOrDefault(player.getUniqueId(), 0L);

        int cooldown = Config.defaultCooldown;
        double velocity = Config.defaultVelocity;
        boolean useSound = Config.defaultUseSound;
        float volume = Config.defaultVolume;
        float pitch = Config.defaultPitch;
        if (Config.useSpecialGroups) {
            for (GroupData group : Config.groups) {
                String permission = "SpitSTIK.group" + group.groupNumber();
                if (!player.hasPermission(permission)) continue;

                cooldown = group.cooldown();
                velocity = group.velocity();
                useSound = group.useSound();
                volume = group.volume();
                pitch = group.pitch();
                break;
            }
        }

        if (currentTime - lastUsed < cooldown) {
            double remaining = Math.max((cooldown - (currentTime - lastUsed)) / 1000.0, 0.1);
            String formatted = String.format("%.1f", remaining);

            player.sendMessage(messages.get("command.cooldownRemaining", Map.of("<cooldownRemaining>", formatted)));
            return true;
        }

        player.launchProjectile(LlamaSpit.class, player.getLocation().getDirection().multiply(velocity));
        if (useSound) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, SoundCategory.HOSTILE, volume, pitch);
        }

        cooldowns.put(player.getUniqueId(), currentTime);
        return true;
    }

    private void reloadConfig(CommandSender sender) {
        if (!sender.hasPermission("SpitSTIK.reload")) {
            sender.sendMessage(messages.get("command.noPermissionToReload"));
            return;
        }

//        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
//            messages.reload();
//            Config.reload();
//        });
        Bukkit.getAsyncScheduler().runNow(plugin, task -> {
            messages.reload();
            Config.reload();
        });

        if (Config.logCoreProtect) {
            Plugin coreProtectPlugin = Bukkit.getPluginManager().getPlugin("CoreProtect");
            if (coreProtectPlugin == null || !coreProtectPlugin.isEnabled()) {
                plugin.getLogger().warning("CoreProtect not found. logCoreProtect set to false");
                Config.logCoreProtect = false;
            } else {
                coreProtectAPI = ((CoreProtect) coreProtectPlugin).getAPI();
            }
        }
        sender.sendMessage(messages.get("configReloaded"));
    }
}
