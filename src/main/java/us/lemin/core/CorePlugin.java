package us.lemin.core;

import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.lemin.core.commands.impl.*;
import us.lemin.core.commands.impl.staff.*;
import us.lemin.core.commands.impl.staff.punish.*;
import us.lemin.core.commands.impl.toggle.ToggleGlobalChatCommand;
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

import java.lang.reflect.Field;
import java.util.Arrays;

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
        serverSettings = new ServerSettings();
        filter = new Filter();
        mongoStorage = new MongoStorage();
        profileManager = new ProfileManager();
        staffManager = new StaffManager(this);
        playerManager = new PlayerManager();
        menuManager = new MenuManager(this);

        registerCommands(
                new BroadcastCommand(),
                new ClearChatCommand(),
                new IgnoreCommand(),
                new ListCommand(),
                new MessageCommand(),
                new RankCommand(),
                new ReplyCommand(),
                new StaffChatCommand(),
                new TeleportCommand(),
                new ToggleMessagesCommand(),
                new ToggleGlobalChatCommand(),
                new ToggleSoundsCommand(),
                new ReportCommand(),
                new HelpOpCommand(),
                new PingCommand(),
                new MuteChatCommand(),
                new SlowChatCommand(),
                new GameModeCommand(),
                new ColorCommand(),
                new ShutdownCommand(),
                new WhitelistCommand(),
                new LinksCommand(),
                new BanCommand(),
                new MuteCommand(),
                new UnbanCommand(),
                new UnmuteCommand(),
                new MuteCommand(),
                new KickCommand(),
                new PunishmentInfoCommand(),
                new AltsCommand(this),
                new BackCommand(),
                new HealCommand(),
                new FeedCommand(),
                new SpeedCommand(),
                new InvSeeCommand(),
                new TpPosCommand()
        );

        registerListeners(
                new PlayerListener(this),
                new MessageListener(this),
                new InventoryListener(this),
                new ServerListener(serverSettings.getCoreConfig())
        );
        getServer().getScheduler().runTaskTimerAsynchronously(this, new BroadcastTask(this), 20L * 60 * 2, 20L * 60L * 2);
    }


    @Override
    public void onDisable() {
        profileManager.saveProfiles();
        serverSettings.saveConfig();

        getServer().getOnlinePlayers().forEach(player -> player.kickPlayer(CC.RED + "The server is restarting."));
    }


    private void registerCommands(Command... commands) {
        try {
            final Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
            final boolean accessible = commandMapField.isAccessible();
            commandMapField.setAccessible(true);
            final CommandMap commandMap = (CommandMap) commandMapField.get(getServer());
            Arrays.stream(commands).forEach(command -> commandMap.register(command.getName(), getName(), command));
            commandMapField.setAccessible(accessible);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("An error occurred while registering commands", e);
        }
    }

    private void registerListeners(Listener... listeners) {
        final PluginManager pluginManager = getServer().getPluginManager();
        Arrays.stream(listeners).forEach(listener -> pluginManager.registerEvents(listener, this));
    }
}