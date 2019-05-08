package us.lemin.core.api.tablistapi.api;

import org.bukkit.entity.Player;

public interface TabTemplate {
    TabLines getLines(Player player);
}
