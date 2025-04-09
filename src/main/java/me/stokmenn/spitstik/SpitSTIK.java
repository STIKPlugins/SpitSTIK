package me.stokmenn.spitstik;

import me.stokmenn.spitstik.commands.SpitCommand;
import me.stokmenn.spitstik.config.Config;
import me.stokmenn.spitstik.config.Messages;
import me.stokmenn.spitstik.listeners.SpitListener;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpitSTIK extends JavaPlugin {

    public static CoreProtectAPI coreProtectAPI;

    @Override
    public void onEnable() {
        PluginManager pluginManager = Bukkit.getPluginManager();

        Config.init(this);
        Messages messages = new Messages(this);

        if (Config.logCoreProtect) {
            Plugin coreProtectPlugin = pluginManager.getPlugin("CoreProtect");
            if (coreProtectPlugin == null || !coreProtectPlugin.isEnabled()) {
                getLogger().warning("CoreProtect not found. logCoreProtect set to false");
                Config.logCoreProtect = false;
            } else {
                coreProtectAPI = ((CoreProtect) coreProtectPlugin).getAPI();
            }
        }

        pluginManager.registerEvents(new SpitListener(messages), this);
        getCommand("spit").setExecutor(new SpitCommand(this, messages));
    }
}
