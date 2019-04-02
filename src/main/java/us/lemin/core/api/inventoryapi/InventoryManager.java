package us.lemin.core.api.inventoryapi;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class InventoryManager implements Listener {
    private final Map<Class<? extends InventoryWrapper>, InventoryWrapper> wrapperClasses = new HashMap<>();
    private final Map<Class<? extends PlayerInventoryWrapper>, PlayerInventoryWrapper> playerWrapperClasses = new HashMap<>();
    private final Map<Inventory, InventoryWrapper> inventoryWrappers = new HashMap<>();
    private final Set<InventoryAction> acceptableActions = new HashSet<>();

    public InventoryManager(Plugin plugin) {
        acceptableActions.addAll(Arrays.asList(InventoryAction.MOVE_TO_OTHER_INVENTORY, InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_SOME));
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            inventoryWrappers.values().forEach(InventoryWrapper::update);
            playerWrapperClasses.values().forEach(playerInventoryWrapper ->
                    playerInventoryWrapper.getOpenPlayerWrappers().forEach((uuid, inventoryWrapper) ->
                    {
                        playerInventoryWrapper.updateInternal(plugin.getServer().getPlayer(uuid), inventoryWrapper);
                    }));
        }, 20L, 20L);
    }

    public void registerPlayerWrapper(PlayerInventoryWrapper wrapper) {
        playerWrapperClasses.put(wrapper.getClass(), wrapper);
    }

    public PlayerInventoryWrapper getPlayerWrapper(Class<? extends PlayerInventoryWrapper> clazz) {
        return playerWrapperClasses.get(clazz);
    }

    public void registerWrapper(InventoryWrapper wrapper) {
        wrapper.init();
        wrapper.update();
        wrapperClasses.put(wrapper.getClass(), wrapper);
        inventoryWrappers.put(wrapper.getInventory(), wrapper);
    }

    public InventoryWrapper getWrapper(Class<? extends InventoryWrapper> clazz) {
        return wrapperClasses.get(clazz);
    }

    public InventoryWrapper getMatchingInventory(Inventory inventory) {
        return inventoryWrappers.get(inventory);
    }



    @EventHandler
    public void onWrapperClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null
                || event.getClickedInventory() == player.getInventory()) {
            return;
        }



        InventoryWrapper wrapper = getMatchingInventory(event.getClickedInventory());

        if (wrapper == null) {
            for (final PlayerInventoryWrapper playerWrapper : playerWrapperClasses.values()) {
                final InventoryWrapper foundWrapper = playerWrapper.getOpenPlayerWrappers().get(player.getUniqueId());

                if (foundWrapper != null) {
                    wrapper = foundWrapper;
                }
            }
        }

        if (wrapper == null) {
            return;
        }

        if (player.getGameMode() != GameMode.SURVIVAL) {
            player.setGameMode(GameMode.SURVIVAL);
        }

        if (!acceptableActions.contains(event.getAction())  || event.getCurrentItem() == null
                || event.getCurrentItem().getType() == Material.AIR) {
            event.setCancelled(true);
            return;
        }

        final Action action = wrapper.getAction(event.getSlot());

        if (action != null) {
            event.setCancelled(true);
            action.onClick(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();

        playerWrapperClasses.values().forEach(playerWrapper -> {
            final InventoryWrapper foundWrapper = playerWrapper.getOpenPlayerWrappers().get(player.getUniqueId());
            if (foundWrapper != null) {
                playerWrapper.close(player);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        playerWrapperClasses.values().forEach(playerWrapper -> {
            final InventoryWrapper foundWrapper = playerWrapper.getOpenPlayerWrappers().get(player.getUniqueId());
            if (foundWrapper != null) {
                playerWrapper.close(player);
            }
        });
    }
}
