package us.lemin.core.api.scoreboardapi;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ScoreboardUpdateEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final List<ScoreboardLine> lines = new LinkedList<>();
    private String title, header = "", footer = "";

    ScoreboardUpdateEvent(Player player, String title) {
        super(player);
        this.title = title;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public String toString() {
        return lines.toString();
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void setSeparator(String separator) {
        setHeader(separator);
        setFooter(separator);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title.length() > 32) {
            title = title.substring(0, 32);
        }

        this.title = ChatColor.translateAlternateColorCodes('&', title);
    }

    public void addLine(String text) {
        if (lines.size() >= ChatColor.values().length) {
            throw new IllegalStateException("Cannot write anymore lines.");
        }

        lines.add(new ScoreboardLine(ChatColor.translateAlternateColorCodes('&', text)));
    }

    public void setLine(int position, String text) {
        if (lines.size() >= ChatColor.values().length) {
            throw new IllegalStateException("Cannot write anymore lines.");
        }

        lines.add(position, new ScoreboardLine(ChatColor.translateAlternateColorCodes('&', text)));
    }

    public ScoreboardLine getLine(int index) {
        return lines.get(index);
    }

    public List<ScoreboardLine> getLines() {
        return Collections.unmodifiableList(lines);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
