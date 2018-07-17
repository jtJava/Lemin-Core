package us.lemin.core.player.rank;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.lemin.core.utils.message.CC;

@Getter
public enum Rank {
    OWNER("Owner", CC.D_RED),
    ADMIN("Admin", CC.RED),
    MOD("Mod", CC.PURPLE),
    TRIAL_MOD("TrialMod", CC.YELLOW),
    BUILDER("Builder", CC.GOLD),
    YOUTUBER("YouTuber", CC.PINK),
    PREMIUM("Premium", "%s✸%s", CC.GREEN),
    VOTER("Voter", CC.PRIMARY + "✔" + CC.SECONDARY, CC.AQUA),
    MEMBER("Member", CC.WHITE, CC.WHITE);

    private final String name;
    private final String rawFormat;
    private final String format;
    private final String color;

    Rank(String name, String color) {
        this(name, CC.D_GRAY + "[%s" + name + CC.D_GRAY + "]%s" + " ", color);
    }

    Rank(String name, String rawFormat, String color) {
        this.name = name;
        this.rawFormat = rawFormat;
        this.format = String.format(rawFormat, color, color);
        this.color = color;
    }

    public static Rank getByName(String name) {
        for (Rank rank : values()) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }

        return null;
    }

    public void apply(Player player) {
        String coloredName = color + player.getName();

        player.setPlayerListName(coloredName);
        player.setDisplayName(coloredName);
    }
}
