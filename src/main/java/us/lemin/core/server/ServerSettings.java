package us.lemin.core.server;

import com.google.common.collect.*;
import lombok.*;
import org.bukkit.*;
import org.bukkit.plugin.*;
import us.lemin.core.*;
import us.lemin.core.storage.flatfile.*;
import us.lemin.core.task.*;

import java.util.*;

@Getter
@Setter
public class ServerSettings {
    public static final String SERVER_DOMAIN = "lemin.us";
    private final Config coreConfig;
    private final String whitelistMessage;
    private WhitelistMode serverWhitelistMode;
    private ShutdownTask shutdownTask;
    private boolean globalChatMuted;
    private int slowChatTime = -1;

    public ServerSettings() {
        this.coreConfig = new Config(CorePlugin.getInstance(), "config");

        coreConfig.addDefaults(ImmutableMap.<String, Object>builder()
                .put("server.motd", "Minecraft Server")
                .put("server.max-players", 1000)
                .put("server.broadcasts", Arrays.asList("1", "2", "3"))
                .put("whitelist.mode", WhitelistMode.NONE.name())
                .put("whitelist.message", "&cThe server is whitelisted. Come back later!")
                .build());
        coreConfig.copyDefaults();

        this.serverWhitelistMode = WhitelistMode.valueOf(coreConfig.getString("whitelist.mode"));
        this.whitelistMessage = ChatColor.translateAlternateColorCodes('&', coreConfig.getString("whitelist.message"));
    }

    public void saveConfig() {
        coreConfig.save();
    }
}
