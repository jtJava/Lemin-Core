package us.lemin.core;

import lombok.*;
import org.bukkit.plugin.*;
import us.lemin.core.commands.impl.*;
import us.lemin.core.commands.impl.staff.*;
import us.lemin.core.commands.impl.staff.punish.*;
import us.lemin.core.commands.impl.toggle.*;
import us.lemin.core.listeners.*;
import us.lemin.core.managers.*;
import us.lemin.core.server.*;
import us.lemin.core.server.filter.*;
import us.lemin.core.storage.database.*;
import us.lemin.core.utils.plugin.*;

@Getter
@Setter
public class Init extends AbstractPlugin {

    @Getter private static Init instance;

    private ServerSettings serverSettings;
    private Filter filter;
    private MongoStorage mongoStorage;
    private ProfileManager profileManager;
    private StaffManager staffManager;
    private PlayerManager playerManager;
    private MenuManager menuManager;

    public Init(final Plugin plugin) {
        super(plugin);
        this.registerCommands();
        this.registerListeners();
        this.registerInstances();
    }

    @Override
    public void registerCommands() {
        this.registerCommand(new BroadcastCommand(CorePlugin.getInstance()),
                new ClearChatCommand(CorePlugin.getInstance()),
                new IgnoreCommand(CorePlugin.getInstance()),
                new ListCommand(),
                new MessageCommand(CorePlugin.getInstance()),
                new RankCommand(CorePlugin.getInstance()),
                new ReplyCommand(CorePlugin.getInstance()),
                new StaffChatCommand(CorePlugin.getInstance()),
                new TeleportCommand(CorePlugin.getInstance()),
                new ToggleMessagesCommand(CorePlugin.getInstance()),
                new ToggleGlobalChat(CorePlugin.getInstance()),
                new ToggleSoundsCommand(CorePlugin.getInstance()),
                new ReportCommand(CorePlugin.getInstance()),
                new HelpOpCommand(CorePlugin.getInstance()),
                new PingCommand(),
                new MuteChatCommand(CorePlugin.getInstance()),
                new SlowChatCommand(CorePlugin.getInstance()),
                new GameModeCommand(),
                new ColorCommand(CorePlugin.getInstance()),
                new ShutdownCommand(CorePlugin.getInstance()),
                new WhitelistCommand(CorePlugin.getInstance()),
                new LinksCommand(),
                new BanCommand(CorePlugin.getInstance()),
                new MuteCommand(CorePlugin.getInstance()),
                new UnbanCommand(CorePlugin.getInstance()),
                new UnmuteCommand(CorePlugin.getInstance()),
                new MuteCommand(CorePlugin.getInstance()),
                new KickCommand(CorePlugin.getInstance()),
                new PunishmentInfoCommand(CorePlugin.getInstance()),
                new AltsCommand(CorePlugin.getInstance()),
                new BackCommand(CorePlugin.getInstance()),
                new HealCommand(),
                new FeedCommand(),
                new SpeedCommand(),
                new InvSeeCommand(),
                new TpPosCommand());
    }

    @Override
    public void registerListeners() {
        this.registerListener(new PlayerListener(CorePlugin.getInstance()),
                new MessageListener(CorePlugin.getInstance()),
                new InventoryListener(CorePlugin.getInstance()),
                new ServerListener(serverSettings.getCoreConfig()));
    }

    @Override
    public void registerInstances() {
        serverSettings = new ServerSettings(CorePlugin.getInstance());
        filter = new Filter();

        mongoStorage = new MongoStorage();

        profileManager = new ProfileManager();
        staffManager = new StaffManager(this);
        playerManager = new PlayerManager();
        menuManager = new MenuManager(CorePlugin.getInstance());
    }
}
