package us.lemin.core.server;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;
import us.lemin.core.CorePlugin;
import us.lemin.core.storage.flatfile.Config;
import us.lemin.core.task.ShutdownTask;
import us.lemin.core.utils.message.CC;

@Getter
public class ServerSettings {
    public static final String SERVER_DOMAIN = "lemin.us";
    private final Config coreConfig;
    private final String whitelistMessage;
    @Setter
    private WhitelistMode serverWhitelistMode;
    @Setter
    private ShutdownTask shutdownTask;
    @Setter
    private boolean globalChatMuted;
    @Setter
    private int slowChatTime = -1;

    public ServerSettings(CorePlugin plugin) {
        this.coreConfig = new Config(plugin, "core");

        coreConfig.addDefaults(ImmutableMap.<String, Object>builder()
                .put("broadcasts", Arrays.asList("1", "2", "3"))
                .put("whitelist.mode", WhitelistMode.NONE.name())
                .put("whitelist.message", CC.RED + "The server is whitelisted. Come back later!")
                .build());
        coreConfig.copyDefaults();

        this.serverWhitelistMode = WhitelistMode.valueOf(coreConfig.getString("whitelist.mode"));
        this.whitelistMessage = coreConfig.getString("whitelist.message");
    }

    public void saveConfig() {
        coreConfig.save();
    }
}
