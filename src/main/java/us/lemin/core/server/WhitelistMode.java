package us.lemin.core.server;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.message.CC;

public enum WhitelistMode {
    NONE {
        @Override
        public boolean isProfileIneligible(CoreProfile profile) {
            return false;
        }
    },
    RANKS {
        @Override
        public boolean isProfileIneligible(CoreProfile profile) {
            return !profile.hasDonor();
        }
    },
    STAFF {
        @Override
        public boolean isProfileIneligible(CoreProfile profile) {
            return !profile.hasStaff();
        }
    };

    private static final String WHITELIST_MESSAGE = CC.RED + "The server has been whitelisted. Come back later!";

    public void activate() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            final CoreProfile profile = CorePlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
            if (isProfileIneligible(profile)) {
                player.kickPlayer(WHITELIST_MESSAGE);
            }
        });
    }

    public abstract boolean isProfileIneligible(CoreProfile profile);
}
