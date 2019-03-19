package us.lemin.core.api.scoreboardapi;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.lemin.core.api.scoreboardapi.api.ScoreboardAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScoreboardApi implements Listener, Runnable {
    private static final String OBJECTIVE_ID = "objective";
    private static final String BELOW_OBJECTIVE_ID = "objectiveBelow";
    private final ScoreboardAdapter adapter;
    private final Plugin plugin;
    private final boolean belowName;

    public ScoreboardApi(Plugin plugin, ScoreboardAdapter adapter, boolean belowName) {
        this.plugin = plugin;
        this.adapter = adapter;
        this.belowName = belowName;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this, 0L, adapter.updateRate());
    }

    public void updateScoreboard(Player player) {
        Scoreboard board = player.getScoreboard();
        Objective objective = board.getObjective(OBJECTIVE_ID);
        Objective objectiveBelow = board.getObjective(BELOW_OBJECTIVE_ID);

        if (objective == null) {
            objective = board.registerNewObjective(OBJECTIVE_ID, "dummy");
            objective.setDisplayName("");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        if (belowName && objectiveBelow == null) {
            objectiveBelow = board.registerNewObjective(BELOW_OBJECTIVE_ID, "dummy");
            objectiveBelow.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }

        ScoreboardUpdateEvent event = new ScoreboardUpdateEvent(player, objective.getDisplayName());

        plugin.getServer().getPluginManager().callEvent(event);

        if (!objective.getDisplayName().equals(event.getTitle())) {
            objective.setDisplayName(event.getTitle());
        }

        List<ScoreboardLine> lines = event.getLines();

        if (lines.size() > 0) {
            if (!event.getHeader().isEmpty()) {
                event.setLine(0, event.getHeader());
            }

            if (!event.getFooter().isEmpty()) {
                event.addLine(event.getFooter());
            }
        }

        List<Team> teams = new ArrayList<>();

        for (int i = 0; i < ChatColor.values().length; i++) {
            if (board.getTeam("#line-" + i) == null) {
                board.registerNewTeam("#line-" + i);
            }

            teams.add(board.getTeam("#line-" + i));
        }

        for (int i = 0; i < lines.size(); i++) {
            Team team = teams.get(i);
            ScoreboardLine line = event.getLine(i);
            String prefix = line.getPrefix();
            String suffix = line.getSuffix();

            if (!team.getPrefix().equals(prefix)) {
                team.setPrefix(prefix);
            }

            if (!team.getSuffix().equals(suffix)) {
                team.setSuffix(line.getSuffix());
            }

            String entry = ChatColor.values()[i] + line.getPrefixFinalColor();
            Set<String> entries = team.getEntries();

            if (entries.size() == 0) {
                team.addEntry(entry);
                objective.getScore(entry).setScore(lines.size() - i);
            } else if (entries.size() == 1) {
                String already = entries.iterator().next();

                if (!entry.equals(already)) {
                    board.resetScores(already);
                    team.removeEntry(already);
                    team.addEntry(entry);
                    objective.getScore(entry).setScore(lines.size() - i);
                } else {
                    objective.getScore(already).setScore(lines.size() - i);
                }
            }
        }

        for (int i = lines.size(); i < ChatColor.values().length; i++) {
            Team team = teams.get(i);
            Set<String> entries = team.getEntries();

            if (entries.size() > 0) {
                for (String entry : entries) {
                    board.resetScores(entry);
                    team.removeEntry(entry);
                }
            }
        }
    }

    @EventHandler
    public void onScoreboardUpdate(ScoreboardUpdateEvent event) {
        adapter.onUpdate(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setScoreboard(plugin.getServer().getScoreboardManager().getNewScoreboard());
        updateScoreboard(player);
    }

    @Override
    public void run() {
        plugin.getServer().getOnlinePlayers().forEach(this::updateScoreboard);
    }
}
