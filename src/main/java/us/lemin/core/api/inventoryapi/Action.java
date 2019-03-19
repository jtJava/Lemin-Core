package us.lemin.core.api.inventoryapi;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Action {
    void onClick(InventoryClickEvent event);
}
