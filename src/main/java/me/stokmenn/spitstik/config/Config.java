package me.stokmenn.spitstik.config;

import me.stokmenn.spitstik.GroupData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Config {
    private static JavaPlugin plugin;

    public static int defaultCooldown;
    public static double defaultVelocity;
    public static boolean defaultUseSound;
    public static boolean defaultDoDamage;
    public static float defaultVolume;
    public static float defaultPitch;

    public static boolean logCoreProtect;
    public static String coreProtectPrefix;

    public static boolean useSpecialGroups;

    public static final List<GroupData> groups = new ArrayList<>();

    public static void init(JavaPlugin plugin) {
        Config.plugin = plugin;
        plugin.saveDefaultConfig();
        reload();
    }

    public static void reload() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        defaultCooldown = config.getInt("defaultCooldown", 10000);
        defaultVelocity = config.getDouble("defaultVelocity", 0.4);
        defaultUseSound = config.getBoolean("defaultUseSound", true);
        defaultDoDamage = config.getBoolean("defaultDoDamage", false);
        defaultVolume = (float) config.getDouble("defaultVolume", 1.0);
        defaultPitch = (float) config.getDouble("defaultPitch", 1.0);

        logCoreProtect = config.getBoolean("logCoreProtect", false);
        coreProtectPrefix = config.getString("coreProtectPrefix", "#SpitSTIK-");

        useSpecialGroups = config.getBoolean("useSpecialGroups", false);

        if (!useSpecialGroups) return;
        groups.clear();
        ConfigurationSection groupsSection = config.getConfigurationSection("groups");
        if (groupsSection == null) return;
        for (String groupKey : groupsSection.getKeys(false)) {
            if (!groupKey.matches("group\\d+")) continue;

            ConfigurationSection groupSection = groupsSection.getConfigurationSection(groupKey);
            if (groupSection == null) continue;

            int groupNumber;
            try {
                groupNumber = Integer.parseInt(groupKey.replace("group", ""));
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid group name: " + groupKey);
                continue;
            }

            int cooldown = groupSection.getInt("cooldown", defaultCooldown);
            double velocity = groupSection.getDouble("velocity", defaultVelocity);
            boolean useSound = groupSection.getBoolean("useSound", defaultUseSound);
            boolean doDamage = groupSection.getBoolean("doDamage", defaultDoDamage);
            float volume = (float) groupSection.getDouble("volume", defaultVolume);
            float pitch = (float) groupSection.getDouble("pitch", defaultPitch);

            groups.add(new GroupData(groupNumber, cooldown, velocity, useSound, doDamage, volume, pitch));
        }

        groups.sort(Comparator.comparingInt(GroupData::groupNumber).reversed());
    }
}