package us.lemin.core.player.rank;

import lombok.Getter;
import org.bukkit.entity.Player;
import us.lemin.core.utils.message.CC;

import java.util.*;

@Getter
public enum Rank {
    OWNER("Owner", CC.D_RED),
    DEV("Dev", CC.AQUA),
    ADMIN("Admin", CC.RED),
    MOD("Mod", CC.PURPLE),
    TRIAL_MOD("Trial Mod", CC.YELLOW),
    BUILDER("Builder", CC.GOLD),
    YOUTUBER("YouTuber", CC.PINK + CC.I),
    PREMIUM("Premium", "%s✸%s", CC.GREEN),
    VOTER("Voter", CC.D_GREEN + "✔" + CC.GREEN, CC.D_GREEN),
    MEMBER("Member", CC.WHITE, CC.WHITE);

    private final String name;
    private final String rawFormat;
    private final String format;
    private final String color;


    Rank(String name, String color) {
        this(name, CC.GRAY + "(%s" + name + CC.GRAY + ")%s" + " ", color);
    }

    Rank(String name, String rawFormat, String color) {
        this.name = name;
        this.rawFormat = rawFormat;
        this.format = String.format(rawFormat, color, color);
        this.color = color;
    }

    public static Rank getByName(String name) {
        return Arrays.stream(values()).filter(rank -> rank.name().equalsIgnoreCase(name)).findFirst().orElse(null);

    }

    public void apply(Player player) {
        String coloredName = color + player.getName();
        player.setDisplayName(coloredName);
    }

}
