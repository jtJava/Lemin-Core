package us.lemin.core.commands.impl.staff;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.StringUtil;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.profile.ProfileUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class AltsCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public AltsCommand(CorePlugin plugin) {
        super("alts", Rank.ADMIN);
        this.plugin = plugin;
        setAliases("dupeip");
        setUsage(CC.RED + "Usage: /alts <name:ip>");
    }

    @Override
    public void execute(Player player, String[] args) {


        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            UUID targetId;
            String targetName;
            Player targetPlayer = plugin.getServer().getPlayer(args[0]);
            CoreProfile targetProfile = null;

            if (targetPlayer == null) {
                ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[0]);

                if (profile == null) {
                    player.sendMessage(Messages.PLAYER_NOT_FOUND);
                    return;
                } else {
                    targetId = profile.getId();
                    targetName = profile.getName();
                }
            } else {
                targetId = targetPlayer.getUniqueId();
                targetName = targetPlayer.getName();
                targetProfile = plugin.getProfileManager().getProfile(targetId);
            }

            LinkedHashSet<String> alts = retrieveAlts(targetProfile);
            String altsString = StringUtil.joinListGrammaticallyWithGuava(new ArrayList<>(alts));


            player.sendMessage(CC.GREEN + "Alts of " + targetName + " are: " + altsString);

        });

    }



    //TODO: cleanup
    public LinkedHashSet<String> retrieveAlts(CoreProfile coreProfile) {
        LinkedHashSet<String> alts = new LinkedHashSet<>();

        List<String> knownAddresses = coreProfile.getKnownAddresses();


        knownAddresses.forEach(knownAddress -> {
            FindIterable<Document> foundDocuments = plugin.getMongoStorage().getDocumentsByFilter("alts", knownAddresses);
            foundDocuments.forEach((Consumer<? super Document>) document -> alts.addAll((List<String>) document.get("known_players")));

        });
        return alts;
    }
}
