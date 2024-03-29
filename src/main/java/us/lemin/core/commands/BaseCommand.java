package us.lemin.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

public abstract class BaseCommand extends Command {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private final Rank requiredRank;
    public CorePlugin plugin = CorePlugin.getInstance();

    protected BaseCommand(String name, Rank requiredRank) {
        super(name);
        this.requiredRank = requiredRank;
    }

    protected BaseCommand(String name) {
        this(name, Rank.MEMBER);
    }

    protected String getSenderName(CommandSender sender) {
        return sender instanceof Player ? ((Player) sender).getDisplayName() : CC.D_RED + "Console";
    }

    @Override
    public final boolean execute(CommandSender sender, String alias, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final CoreProfile profile = CorePlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());

            if (!profile.hasRank(requiredRank)) {
                player.sendMessage(CC.RED + "You don't have the required rank to perform this command.");
                return true;
            }
        }

        execute(sender, args);
        return true;
    }

    protected final void setAliases(String... aliases) {
        if (aliases.length > 0) {
            setAliases(aliases.length == 1 ? Collections.singletonList(aliases[0]) : Arrays.asList(aliases));
        }
    }

    protected final void setUsage(String... uses) {
        final StringBuilder builder = new StringBuilder();

        IntStream.range(0, uses.length).forEach(i -> {
            final String use = uses[i];
            builder.append(use);
            if (i + 1 != uses.length) {
                builder.append(LINE_SEPARATOR);
            }
        });

        setUsage(builder.toString());
    }

    protected abstract void execute(CommandSender sender, String[] args);
}
