package us.lemin.core;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.lemin.core.commands.impl.ClearChatCommand;
import us.lemin.core.commands.impl.ColorCommand;
import us.lemin.core.commands.impl.HelpOpCommand;
import us.lemin.core.commands.impl.IgnoreCommand;
import us.lemin.core.commands.impl.ListCommand;
import us.lemin.core.commands.impl.MessageCommand;
import us.lemin.core.commands.impl.PingCommand;
import us.lemin.core.commands.impl.ReplyCommand;
import us.lemin.core.commands.impl.ReportCommand;
import us.lemin.core.commands.impl.staff.BroadcastCommand;
import us.lemin.core.commands.impl.staff.FreezeCommand;
import us.lemin.core.commands.impl.staff.GameModeCommand;
import us.lemin.core.commands.impl.staff.MuteChatCommand;
import us.lemin.core.commands.impl.staff.RankCommand;
import us.lemin.core.commands.impl.staff.ShutdownCommand;
import us.lemin.core.commands.impl.staff.SlowChatCommand;
import us.lemin.core.commands.impl.staff.StaffChatCommand;
import us.lemin.core.commands.impl.staff.TeleportCommand;
import us.lemin.core.commands.impl.staff.VanishCommand;
import us.lemin.core.commands.impl.staff.WhitelistCommand;
import us.lemin.core.commands.impl.staff.punish.BanCommand;
import us.lemin.core.commands.impl.staff.punish.KickCommand;
import us.lemin.core.commands.impl.staff.punish.MuteCommand;
import us.lemin.core.commands.impl.staff.punish.PunishmentInfoCommand;
import us.lemin.core.commands.impl.staff.punish.UnbanCommand;
import us.lemin.core.commands.impl.staff.punish.UnmuteCommand;
import us.lemin.core.commands.impl.toggle.ToggleGlobalChat;
import us.lemin.core.commands.impl.toggle.ToggleMessagesCommand;
import us.lemin.core.commands.impl.toggle.ToggleSoundsCommand;
import us.lemin.core.listeners.InventoryListener;
import us.lemin.core.listeners.MessageListener;
import us.lemin.core.listeners.PlayerListener;
import us.lemin.core.listeners.ServerListener;
import us.lemin.core.managers.MenuManager;
import us.lemin.core.managers.PlayerManager;
import us.lemin.core.managers.ProfileManager;
import us.lemin.core.managers.StaffManager;
import us.lemin.core.server.ServerSettings;
import us.lemin.core.server.filter.Filter;
import us.lemin.core.storage.database.MongoStorage;
import us.lemin.core.task.BroadcastTask;
import us.lemin.core.utils.message.CC;

@Getter
public class CorePlugin extends JavaPlugin {
    @Getter
    private static CorePlugin instance;

    private ServerSettings serverSettings;
    private Filter filter;
    private MongoStorage mongoStorage;

    private ProfileManager profileManager;
    private StaffManager staffManager;
    private PlayerManager playerManager;
    private MenuManager menuManager;

    @Override
    public void onEnable() {
        instance = this;

        serverSettings = new ServerSettings(this);
        filter = new Filter();

        mongoStorage = new MongoStorage();

        profileManager = new ProfileManager();
        staffManager = new StaffManager(this);
        playerManager = new PlayerManager();
        menuManager = new MenuManager(this);

        registerCommands(
                new BroadcastCommand(this),
                new ClearChatCommand(this),
                new IgnoreCommand(this),
                new ListCommand(),
                new MessageCommand(this),
                new RankCommand(this),
                new ReplyCommand(this),
                new StaffChatCommand(this),
                new TeleportCommand(this),
                new ToggleMessagesCommand(this),
                new ToggleGlobalChat(this),
                new ToggleSoundsCommand(this),
                new VanishCommand(this),
                new ReportCommand(this),
                new HelpOpCommand(this),
                new PingCommand(),
                new BanCommand(this),
                new MuteCommand(this),
                new UnbanCommand(this),
                new UnmuteCommand(this),
                new KickCommand(this),
                new PunishmentInfoCommand(this),
                new MuteChatCommand(this),
                new SlowChatCommand(this),
                new GameModeCommand(),
                new ColorCommand(this),
                new ShutdownCommand(this),
                new FreezeCommand(),
                new WhitelistCommand(this)
        );
        registerListeners(
                new PlayerListener(this),
                new MessageListener(this),
                new InventoryListener(this),
                new ServerListener(serverSettings.getCoreConfig())
        );

        getServer().getScheduler().runTaskTimerAsynchronously(this, new BroadcastTask(this), 20 * 30L, 20 * 30L);
    }

    @Override
    public void onDisable() {
        profileManager.saveProfiles();
        serverSettings.saveConfig();

        for (Player player : getServer().getOnlinePlayers()) {
            player.kickPlayer(CC.RED + "The server is restarting.");
        }
    }

    private void registerCommands(Command... commands) {
        CommandMap commandMap = getServer().getCommandMap();

        for (Command command : commands) {
            commandMap.register(getName(), command);
        }
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();

        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }
}
