package us.lemin.core.api.tablistapi.tab.lines;



import org.bukkit.entity.Player;
import us.lemin.core.api.tablistapi.api.TabLines;
import us.lemin.core.api.tablistapi.api.TabTemplate;

public class DefaultTemplate implements TabTemplate {

    @Override
    public TabLines getLines(Player player) {
        return new TabLines(player);
    }
}
