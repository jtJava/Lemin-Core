package us.lemin.core.inventory.menu.impl;

import org.bukkit.entity.*;
import us.lemin.core.inventory.menu.*;

public abstract class PerPlayerMenu extends Menu {
    protected PerPlayerMenu(int rows, String name) {
        super(rows, name);
    }

    @Override
    public final void update() {
        getInventory().getViewers().stream().map(entity -> (Player) entity).forEach(player -> {
            updatePlayer(player);
            player.updateInventory();
        });
    }

    @Override
    public final void open(Player player) {
        super.open(player);
        updatePlayer(player);
    }

    public abstract void updatePlayer(Player player);
}
