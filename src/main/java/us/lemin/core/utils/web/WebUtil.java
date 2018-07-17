package us.lemin.core.utils.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import lombok.experimental.UtilityClass;
import org.bukkit.plugin.java.JavaPlugin;
import us.lemin.core.callback.WebCallback;

@UtilityClass
public class WebUtil {
    public static void getResponse(JavaPlugin plugin, String url, WebCallback callback) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
                callback.callback(reader.readLine());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
