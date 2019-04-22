/*package us.lemin.core;

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
        this.registerCommand(new BroadcastCommand(),
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
                new AltsCommand(),
                new BackCommand(),
                new HealCommand(),
                new FeedCommand(),
                new SpeedCommand(),
                new InvSeeCommand(),
                new TpPosCommand());
    }

    @Override
    public void registerListeners() {
        this.registerListener(new PlayerListener(CorePlugin.getInstance()),
                new MessageListener(),
                new InventoryListener(),
                new ServerListener(serverSettings.getCoreConfig()));
    }

    @Override
    public void registerInstances() {
        serverSettings = new ServerSettings();
        filter = new Filter();

        mongoStorage = new MongoStorage();

        profileManager = new ProfileManager();
        staffManager = new StaffManager(this);
        playerManager = new PlayerManager();
        menuManager = new MenuManager(CorePlugin.getInstance());
    }
}
*/