package us.lemin.core.api.inventoryapi;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class PlayerAction implements Action {
    private final Consumer<Player> consumer;
    private final boolean autoClose;

    public PlayerAction(Consumer<Player> player) {
        this(player, false);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        consumer.accept(player);

        if (autoClose) {
            player.closeInventory();
        }
    }
}
