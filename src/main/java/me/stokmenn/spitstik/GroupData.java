package me.stokmenn.spitstik;

import org.bukkit.Material;

import java.util.Set;

public record GroupData (
        int groupNumber,
        int cooldown,
        double velocity,
        boolean useSound,
        boolean doDamage,
        float volume,
        float pitch,
        Set<Material> materials
) {}
