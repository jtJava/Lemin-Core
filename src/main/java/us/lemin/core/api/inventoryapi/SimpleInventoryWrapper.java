package us.lemin.core.api.inventoryapi;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SimpleInventoryWrapper implements InventoryWrapper {
    private final Map<Integer, Action> actions = new HashMap<>();
    private final Inventory inventory;
    private final String name;
    private final int rows;

    public SimpleInventoryWrapper(String name, int rows) {
        this.name = name;
        this.rows = rows;
        this.inventory = Bukkit.createInventory(null, rows * 9, name);
    }

    private int getFirstEmptySlot() {
        int slot = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                return slot;
            }

            slot++;
        }

        return slot;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Action getAction(int slot) {
        return actions.get(slot);
    }

    @Override
    public void setItem(int slot, ItemStack item, Action action) {
        inventory.setItem(slot, item);
        actions.put(slot, action);
    }

    @Override
    public void setItem(int row, int column, ItemStack item, Action action) {
        setItem(((row - 1) * 9) + (column - 1), item, action);
    }

    @Override
    public void fillBorder(ItemStack item) {
        for (int row = 1; row <= rows; row++) {
            for (int column = 1; column <= 9; column++) {
                if (row == 1 || row == rows || column == 1 || column == 9) {
                    setItem(row, column, item, e -> {
                    });
                }
            }
        }
    }

    @Override
    public void addItem(ItemStack item, Action action) {
        setItem(getFirstEmptySlot(), item, action);
    }

    protected List<HumanEntity> getViewers() {
        return inventory.getViewers();
    }

    @Override
    public void open(Player player) {
        player.openInventory(inventory);
    }

    public void clear() {
        inventory.clear();

    }
}
