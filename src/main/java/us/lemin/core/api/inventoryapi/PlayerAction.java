package us.lemin.core.api.inventoryapi;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class PlayerAction implements Action {
    private final BiConsumer<Player, ClickType> consumer;
    private final boolean autoClose;

    public PlayerAction(BiConsumer<Player, ClickType> biConsumer) {
        this(biConsumer, false);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ClickType clickType = event.getClick();
        final Player player = (Player) event.getWhoClicked();
        consumer.accept(player, clickType);

        if (autoClose) {
            player.closeInventory();
        }
    }
}
