package us.lemin.core.punishment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

@AllArgsConstructor
public enum PunishmentType {
    MUTE(
            "temporarily muted",
            "unmuted",
            ChatColor.RED + "You are currently muted for %DURATION%.",
            null
    ),
    TEMPBAN(
            "temporarily banned",
            "unbanned",
            "\n" + ChatColor.RED + "Your account has been temporarily suspended from {server_name}.\nExpires in %EXPIRES%.\n\n" + PunishmentMessages.APPEAL_FOOTER,
            "\n" + ChatColor.RED + "Your account has been temporarily suspended from {server_name} for a punishment related to {player}.\nExpires in {expire}.\n\n" + PunishmentMessages.APPEAL_FOOTER
    ),
    BAN(
            "permanently banned",
            "unbanned",
            "\n" + ChatColor.RED + "Your account has been suspended from {server_name}.\n\n" + PunishmentMessages.APPEAL_FOOTER,
            "\n" + ChatColor.RED + "Your account has been suspended from {server_name} for a punishment related to {player}.\n\n" + PunishmentMessages.APPEAL_FOOTER
    ),
    BLACKLIST(
            "blacklisted",
            "unblacklisted",
            "\n" + ChatColor.RED + "Your account has been blacklisted from {server_name}.\n\nThis type of punishment cannot be appealed.",
            "\n" + ChatColor.RED + "Your account has been blacklisted from {server_name} for a punishment related to {player}.\n\nThis type of punishment cannot be appealed."
    );

    @Getter
    private String context;
    @Getter
    private String undoContext;
    @Getter
    private String message;
    @Getter
    private String sharedMessage;

}
