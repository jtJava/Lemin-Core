package us.lemin.core.punishment;

import net.md_5.bungee.api.ChatColor;

public class PunishmentMessages {
    public static final String BROADCAST = ChatColor.RESET + "{target_name} "
            + ChatColor.GREEN + "has been {context} by " + ChatColor.RESET + "{staff_name}";
    public static final String APPEAL_FOOTER = "To appeal, visit http://{server_site}.";
}
