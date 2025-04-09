package me.stokmenn.spitstik.listeners;

import me.stokmenn.spitstik.GroupData;
import me.stokmenn.spitstik.config.Config;
import me.stokmenn.spitstik.config.Messages;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Map;

import static me.stokmenn.spitstik.SpitSTIK.coreProtectAPI;

public class SpitListener implements Listener {

    private final Messages messages;

    public SpitListener(Messages messages) {
        this.messages = messages;
    }

    @EventHandler
    public void OnPlayerSpitPlayer(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player attacker)) return;
        if (!(event.getHitEntity() instanceof Player victim)) return;
        if (!(event.getEntity() instanceof LlamaSpit)) return;

        victim.sendMessage(messages.get("listener.spitReceive", Map.of("<player>", attacker.getName())));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!Config.logCoreProtect) return;

        if (!(event.getEntity() instanceof ItemFrame itemFrame)) return;
        if (!(event.getDamager() instanceof LlamaSpit spit)) return;
        if (!(spit.getShooter() instanceof Player player)) return;
        Material itemType = itemFrame.getItem().getType();
        if (itemType == Material.AIR) return;

        coreProtectAPI.logRemoval(Config.coreProtectPrefix + player.getName(), itemFrame.getLocation(), itemType, null);
    }

    @EventHandler
    public void OnSpit(ProjectileHitEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof LlamaSpit)) return;

        boolean doDamage = Config.defaultDoDamage;
        for (GroupData group : Config.groups) {
            String permission = "SpitSTIK.group" + group.groupNumber();
            if (attacker.hasPermission(permission)) {
                doDamage = group.doDamage();
                break;
            }
        }
        if (!doDamage) event.setCancelled(true);
    }
}
