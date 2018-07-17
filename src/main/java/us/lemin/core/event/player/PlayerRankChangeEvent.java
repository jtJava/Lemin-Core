package us.lemin.core.event.player;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;

@Getter
public class PlayerRankChangeEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final CoreProfile profile;
    private final Rank newRank;

    public PlayerRankChangeEvent(Player who, CoreProfile profile, Rank newRank) {
        super(who);
        this.profile = profile;
        this.newRank = newRank;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
