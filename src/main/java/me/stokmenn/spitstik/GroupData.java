package me.stokmenn.spitstik;

public record GroupData (
        int groupNumber,
        int cooldown,
        double velocity,
        boolean useSound,
        boolean doDamage,
        float volume,
        float pitch
) {}
