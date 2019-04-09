package us.lemin.core.inventory.menu.impl;

import org.bukkit.Material;
import us.lemin.core.*;
import us.lemin.core.inventory.menu.Menu;
import us.lemin.core.inventory.menu.action.Action;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.timer.Timer;

public class ReportMenu extends Menu {
    private final CorePlugin plugin;
    private final Init init;

    public ReportMenu(CorePlugin plugin) {
        super(1, "Select a Report Reason");
        this.plugin = plugin;
        init = new Init(plugin);
    }

    private Action getAction(String reason) {
        return player -> {
            final CoreProfile profile = init.getProfileManager().getProfile(player.getUniqueId());
            final Timer cooldownTimer = profile.getReportCooldownTimer();

            if (cooldownTimer.isActive()) {
                player.sendMessage(CC.RED + "You can't report a player for another " + cooldownTimer.formattedExpiration() + ".");
                player.closeInventory();
                return;
            }

            final String targetName = profile.getReportingPlayerName();

            init.getStaffManager().messageStaff("");
            init.getStaffManager().messageStaff(CC.RED + "(Report) " + CC.SECONDARY + player.getName() + CC.PRIMARY
                    + " reported " + CC.SECONDARY + targetName + CC.PRIMARY + " for " + CC.SECONDARY + reason + CC.PRIMARY + ".");
            init.getStaffManager().messageStaff("");

            player.sendMessage(CC.GREEN + "Report sent for " + targetName + CC.GREEN + ": " + CC.R + reason);
            player.closeInventory();
        };
    }

    @Override
    public void setup() {
    }

    @Override
    public void update() {
        setActionableItem(1, new ItemBuilder(Material.DIAMOND_SWORD).name(CC.PRIMARY + "Combat Cheats").build(), getAction("Combat Cheats"));
        setActionableItem(3, new ItemBuilder(Material.DIAMOND_BOOTS).name(CC.PRIMARY + "Movement Cheats").build(), getAction("Movement Cheats"));
        setActionableItem(5, new ItemBuilder(Material.PAPER).name(CC.PRIMARY + "Chat Violation").build(), getAction("Chat Violation"));
        setActionableItem(7, new ItemBuilder(Material.NETHER_STAR).name(CC.PRIMARY + "Assistance").build(), getAction("I need assistance related to this player"));
    }
}
