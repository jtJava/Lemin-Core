package us.lemin.core.utils.player;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@UtilityClass
public class PlayerUtil {
    public static void clearPlayer(Player player) {
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        Inventory craftingInventory = entityPlayer.activeContainer.getBukkitView().getTopInventory();

        if (craftingInventory instanceof CraftingInventory) {
            craftingInventory.clear();
        }

        player.setHealth(player.getMaxHealth());
        player.setMaximumNoDamageTicks(20);
        player.setFallDistance(0.0F);
        player.setWalkSpeed(.2F);
        player.setFoodLevel(20);
        player.setSaturation(5.0F);
        player.setFireTicks(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setItemOnCursor(null);
        player.updateInventory();
    }

    public static int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    public static boolean hasEffect(Player player, PotionEffectType type) {
        return player.getActivePotionEffects().stream().anyMatch(effect -> effect.getType().equals(type));
    }
}
