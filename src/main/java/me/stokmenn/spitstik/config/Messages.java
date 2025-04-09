package me.stokmenn.spitstik.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Messages {
    private final File configFile;
    private YamlConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Map<String, Component> messages = new HashMap<>();

    public Messages(JavaPlugin plugin) {
        this.configFile = new File(plugin.getDataFolder(), "messages.yml");
        plugin.saveResource("messages.yml", false);
        reload();
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
        messages.clear();

        initMessage("configReloaded", "<green>✔ Config has been reloaded");

        initMessage("listener.spitReceive", "Player <player> spat on you");

        initMessage("command.onlyPlayerCanUse", "<red>✘ This command can only be used by a player");
        initMessage("command.noPermissionToUse", "<red>✘ You do not have permission to use this command");
        initMessage("command.noPermissionToReload", "<red>✘ You do not have permission to reload the SpitSTIK plugin");
        initMessage("command.cooldownRemaining", "<red>✘ Please wait <cooldownRemaining>s.");
    }

    private void initMessage(String path, String def) {
        messages.put(path, miniMessage.deserialize(config.getString(path, def)));
    }

    public Component get(String key) {
        return messages.get(key);
    }

    public Component get(String key, Map<String, Object> placeholders) {
        Component message = messages.get(key);

        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            message = message.replaceText(builder ->
                    builder.match(entry.getKey())
                            .replacement(Component.text(entry.getValue().toString()))
            );
        }

        return message;
    }
}