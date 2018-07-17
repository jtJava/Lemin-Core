package us.lemin.core.commands.impl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.ColorPair;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.CustomColorPair;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

public class ColorCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public ColorCommand(CorePlugin plugin) {
        super("color", Rank.EXCLUSIVE);
        this.plugin = plugin;

        StringBuilder colors = new StringBuilder(CC.RED + "Usage: /color [primary|secondary] <color>\nValid colors are: ");

        int colorCount = ColorPair.values().length;
        int currentCount = 0;

        for (ColorPair pair : ColorPair.values()) {
            colorCount++;

            String name = pair.getName();

            if (name == null) {
                continue;
            }

            ChatColor color = ChatColor.valueOf(name);

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
        for (ChatColor color : ChatColor.values()) {
            if (name.equalsIgnoreCase(color.name())) {
                return color;
            }
        }

        return null;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        String arg = args[0].toLowerCase();
        CoreProfile profile = plugin.getProfileManager().getProfile(player.getUniqueId());

        if (arg.equals("reset")) {
            profile.setColorPair(new CustomColorPair());
            player.setDisplayName(profile.getRank().getColor() + player.getName());
            player.sendMessage(CC.GREEN + "Your colors have been reset.");
            return;
        }

        CustomColorPair customPair = profile.getColorPair();

        if (args.length > 1) {
            ChatColor matchedColor = getMatchingChatColor(args[1]);

            if (matchedColor == null) {
                player.sendMessage(usageMessage);
                return;
            }

            switch (arg) {
                case "primary":
                    customPair.setPrimary(matchedColor);
                    customPair.apply(player);
                    player.sendMessage(CC.GREEN + "Set primary color to " + matchedColor + getColorName(matchedColor) + CC.GREEN + ".");
                    break;
                case "secondary":
                    customPair.setSecondary(matchedColor);
                    customPair.apply(player);
                    player.sendMessage(CC.GREEN + "Set secondary color to " + matchedColor + getColorName(matchedColor) + CC.GREEN + ".");
                    break;
                default:
                    player.sendMessage(usageMessage);
                    break;
            }
        } else {
            ColorPair matchedPair = ColorPair.getByName(arg);

            if (matchedPair == null) {
                player.sendMessage(usageMessage);
                return;
            }

            customPair.setPrimary(matchedPair.getPrimary());
            customPair.setSecondary(matchedPair.getSecondary());
            customPair.apply(player);

            player.sendMessage(CC.GREEN + "Set your colors to " + customPair.getPrimary() + getColorName(customPair.getPrimary()) + " (primary)"
                    + CC.GREEN + " and " + customPair.getSecondary() + getColorName(customPair.getSecondary()) + " (secondary)" + CC.GREEN + ".");
        }
    }
}
