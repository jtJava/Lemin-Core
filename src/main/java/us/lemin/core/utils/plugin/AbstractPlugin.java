package us.lemin.core.utils.plugin;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;

import java.lang.reflect.*;
import java.util.*;

public abstract class AbstractPlugin {

    private Plugin plugin;

    public AbstractPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public abstract void registerCommands();

    public abstract void registerListeners();

    public abstract void registerInstances();

    protected void registerCommand(final Command... commands) {
        try {
            final Field commandMapField = plugin.getServer().getClass().getDeclaredField("commandMap");
            final boolean accessible = commandMapField.isAccessible();

            commandMapField.setAccessible(true);

            final CommandMap commandMap = (CommandMap) commandMapField.get(plugin.getServer());

            Arrays.stream(commands).forEach(command -> commandMap.register(command.getName(), plugin.getName(), command));

            commandMapField.setAccessible(accessible);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("An error occurred while registering commands", e);
        }
    }

    protected void registerListener(final Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this.plugin));
    }
}
