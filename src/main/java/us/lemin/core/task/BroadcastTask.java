package us.lemin.core.task;

import lombok.RequiredArgsConstructor;
import org.bukkit.Server;
import us.lemin.core.utils.message.CC;

@RequiredArgsConstructor
public class BroadcastTask implements Runnable {
    private static final String[] MESSAGES = {

    };
    private final Server server;
    private int currentIndex = -1;

    @Override
    public void run() {
        if (++currentIndex >= MESSAGES.length) {
            currentIndex = 0;
        }

        String message = MESSAGES[currentIndex];

        server.broadcastMessage("");
        server.broadcastMessage(CC.SECONDARY + "[LeminBot] " + CC.PRIMARY + message);
        server.broadcastMessage("");
    }
}
