package us.lemin.core.managers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class StaffManager {
    @Getter
    private final Set<UUID> staffIds = new HashSet<>();
    private final CorePlugin plugin;

    public void addCachedStaff(UUID id) {
        staffIds.add(id);
    }

    public boolean isInStaffCache(UUID id) {
        return staffIds.contains(id);
    }

    public void removeCachedStaff(UUID id) {
        staffIds.remove(id);
    }

    public void messageStaff(String displayName, String msg) {
        String formattedMsg = CC.GREEN + "[Staff] " + displayName + CC.R + ": " + msg;
        messageStaff(formattedMsg);
    }

    public void messageStaff(Rank requiredRank, String msg) {
        for (UUID id : staffIds) {
            CoreProfile profile = plugin.getProfileManager().getProfile(id);

            if (profile != null && profile.hasRank(requiredRank)) {
                Player loopPlayer = plugin.getServer().getPlayer(profile.getId());

                if (loopPlayer != null && loopPlayer.isOnline()) {
                    loopPlayer.sendMessage(msg);
                }
            }
        }
    }

    public void messageStaff(String msg) {
        messageStaff(Rank.MEMBER, msg);
    }

    public void messageStaffWithPrefix(String msg) {
        messageStaff(CC.GREEN + "[Staff] " + msg);
    }

    public void hideVanishedStaffFromPlayer(Player player) {
        if (!plugin.getProfileManager().getProfile(player.getUniqueId()).hasStaff()) {
            for (UUID id : staffIds) {
                CoreProfile profile = plugin.getProfileManager().getProfile(id);
                if (profile == null) {
                    return;
                }
                if (profile.isVanished()) {
                    Player loopPlayer = plugin.getServer().getPlayer(profile.getId());
                    if (loopPlayer != null && loopPlayer.isOnline()) {
                        player.hidePlayer(loopPlayer);
                    }
                } else {
                    Player loopPlayer = plugin.getServer().getPlayer(profile.getId());

                }
            }
        }
    }
}
