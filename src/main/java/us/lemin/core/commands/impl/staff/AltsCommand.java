package us.lemin.core.commands.impl.staff;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import us.lemin.core.*;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.StringUtil;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.profile.ProfileUtil;

import java.util.*;
import java.util.function.Consumer;

public class AltsCommand extends BaseCommand {
    private final CorePlugin plugin;
    private final Init init;

    public AltsCommand(CorePlugin plugin) {
        super("alts", Rank.ADMIN);
        this.plugin = plugin;
        init = new Init(plugin);
        setAliases("dupeip");
        setUsage(CC.RED + "Usage: /alts <name:ip>");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {


        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            final UUID targetId;
            final String targetName;
            final Player targetPlayer = plugin.getServer().getPlayer(args[0]);
            CoreProfile targetProfile = null;

            if (targetPlayer == null) {
                final ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[0]);

                if (profile == null) {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return;
                } else {
                    targetId = profile.getId();
                    targetName = profile.getName();
                }
            } else {
                targetId = targetPlayer.getUniqueId();
                targetName = targetPlayer.getName();
                targetProfile = init.getProfileManager().getProfile(targetId);
            }

            final LinkedHashSet<String> alts = retrieveAlts(targetProfile);
            final LinkedHashSet<String> testingAlts = retrieveFullAlts(targetProfile);
            final String altsString = StringUtil.joinListGrammaticallyWithGuava(new ArrayList<>(alts));
            final String altsStringTest = StringUtil.joinListGrammaticallyWithGuava(new ArrayList<>(testingAlts));


            sender.sendMessage(CC.GREEN + "Alts of " + targetName + " are: " + altsString);
            sender.sendMessage(CC.GREEN + "Alts of " + targetName + " are: " + altsStringTest);

        });

    }



    //TODO: cleanup
    private LinkedHashSet<String> retrieveAlts(CoreProfile coreProfile) {
        final LinkedHashSet<String> alts = new LinkedHashSet<>();

        final List<String> knownAddresses = coreProfile.getKnownAddresses();


        knownAddresses.forEach(knownAddress -> {
            final FindIterable<Document> foundDocuments = init.getMongoStorage().getDocumentsByFilter("alts", knownAddresses);
            foundDocuments.forEach((Consumer<? super Document>) document -> alts.addAll((List<String>) document.get("known_players")));

        });
        return alts;
    }

    //TODO: test please, or atleast fix the logic i wrote this at 3 am and
    // i dont have much experience with mongo, function is supposed to get all
    // alts related to the initial target, and then all alts related to the ips
    // of those alts until it runs out of relations.
    private LinkedHashSet<String> retrieveFullAlts(CoreProfile coreProfile) {
        final LinkedHashSet<String> alts = new LinkedHashSet<>();

        final List<String> knownAddresses = coreProfile.getKnownAddresses();
        final ListIterator<String> iterator = knownAddresses.listIterator();

        while (iterator.hasNext()) {
            final String knownAddress = iterator.next();
            final FindIterable<Document> foundDocuments = init.getMongoStorage().getDocumentsByFilter("alts", knownAddresses);
            foundDocuments.forEach((Consumer<? super Document>) document -> {
                final List<String> altsOfInitialAddress = (List<String>) document.get("known_players");
                alts.addAll(altsOfInitialAddress);
                altsOfInitialAddress.forEach(altPlayer -> {
                    final Document altDocument = init.getMongoStorage().getDocumentByFilter("players", "name", altPlayer);
                    final List<String> altsKnownAddresses = (List<String>) altDocument.get("known_addresses");
                    altsKnownAddresses.forEach(iterator::add);
                });
            });
        }
        return alts;

    }
}
