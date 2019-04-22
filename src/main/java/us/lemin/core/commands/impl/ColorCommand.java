package us.lemin.core.commands.impl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.ColorPair;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.CustomColorPair;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

import java.util.*;

public class ColorCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public ColorCommand(CorePlugin plugin) {
        super("color", Rank.PREMIUM);
        this.plugin = plugin;

        final StringBuilder colors = new StringBuilder(CC.RED + "Usage: /color [primary|secondary] <color>\nValid colors are: ");

        int colorCount = ColorPair.values().length;
        final int currentCount = 0;

        for (ColorPair pair : ColorPair.values()) {
            colorCount++;

            final String name = pair.getName();

            if (name == null) {
                continue;
            }

            final ChatColor color = ChatColor.valueOf(name);

            colors.append(color.toString()).append(color.name().toLowerCase());

            if (currentCount != colorCount) {
                colors.append(", ");
            }
        }

        setUsage(colors.toString());
    }

    private static String getColorName(ChatColor color) {
        return color.name().toLowerCase().replace("_", " ");
    }

    private static ChatColor getMatchingChatColor(String name) {
        return Arrays.stream(ChatColor.values()).filter(color -> name.equalsIgnoreCase(color.name())).findFirst().orElse(null);

    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        final String arg = args[0].toLowerCase();
        final CoreProfile profile = plugin.getProfileManager().getProfile(player.getUniqueId());

        if (arg.equals("reset")) {
            profile.setColorPair(new CustomColorPair());
            player.setDisplayName(profile.getRank().getColor() + player.getName());
            player.sendMessage(CC.GREEN + "Your colors have been reset.");
            return;
        }

        final CustomColorPair customPair = profile.getColorPair();

        if (args.length > 1) {
            final ChatColor matchedColor = getMatchingChatColor(args[1]);

            if (matchedColor == null) {
                player.sendMessage(usageMessage);
                return;
            }

            switch (arg) {
                case "primary":
                    customPair.setPrimary(matchedColor);
                    player.sendMessage(CC.GREEN + "Set primary color to " + matchedColor + getColorName(matchedColor) + CC.GREEN + ".");
                    break;
                case "secondary":
                    customPair.setSecondary(matchedColor);
                    player.sendMessage(CC.GREEN + "Set secondary color to " + matchedColor + getColorName(matchedColor) + CC.GREEN + ".");
                    break;
                default:
                    player.sendMessage(usageMessage);
                    break;
            }
        } else {
            final ColorPair matchedPair = ColorPair.getByName(arg);

            if (matchedPair == null) {
                player.sendMessage(usageMessage);
                return;
            }

            customPair.setPrimary(matchedPair.getPrimary());
            customPair.setSecondary(matchedPair.getSecondary());

            player.sendMessage(CC.GREEN + "Set your colors to " + customPair.getPrimary() + getColorName(customPair.getPrimary()) + " (primary)"
                    + CC.GREEN + " and " + customPair.getSecondary() + getColorName(customPair.getSecondary()) + " (secondary)" + CC.GREEN + ".");
        }
    }
}
