package us.lemin.core.api.inventoryapi;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface InventoryWrapper {
    String getName();

    int getRows();

    Inventory getInventory();

    Action getAction(int slot);

    void setItem(int slot, ItemStack item, Action action);

    void setItem(int row, int column, ItemStack item, Action action);

    void fillBorder(ItemStack item);

    void addItem(ItemStack item, Action action);

    void init();

    void update();

    void open(Player player);

    void clear();
}
