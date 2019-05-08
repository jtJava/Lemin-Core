package us.lemin.core.api.tablistapi.api;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import protocolsupport.api.ProtocolSupportAPI;
import us.lemin.core.api.tablistapi.tab.TabList;

public class TabLines {
    private final String[] lines = new String[80];
    private final TabList.Version version;

    public TabLines(Player player) {
        this.version = ProtocolSupportAPI.getProtocolVersion(player).getId() > 5 ? TabList.Version.CURRENT : TabList.Version.LEGACY;

        for (int i = 0; i < lines.length; i++) {
            lines[i] = "";
        }
    }

    private int getFirstEmptyIndex(int startIndex) {
        for (int i = startIndex; i < lines.length; i++) {
            if (lines[i].isEmpty()) {
                return i;
            }
        }

        return -1;
    }

    private int getFirstEmptyIndex() {
        return getFirstEmptyIndex(0);
    }

    private int getPosition(int column, int row) {
        if (row < 1 || row > 20) {
            throw new IllegalArgumentException("Illegal row: " + row);
        }

        return version == TabList.Version.LEGACY ? (column > 3 ? 79 : (column - 1) + ((row - 1) * 3)) : (20 * (column - 1)) + (row - 1);
    }

    private void setLineByIndex(int index, String entry) {
        if (entry.length() > 32) {
            throw new IllegalArgumentException("Can't have lines longer than 32 characters");
        }

        lines[index] = ChatColor.translateAlternateColorCodes('&', entry);
    }

    public void set(int x, int y, String entry) {
        setLineByIndex(getPosition(x, y), entry);
    }

    public TabLines add(String entry) {
        int index = getFirstEmptyIndex();

        System.out.println(index);

        if (index != -1) {
            setLineByIndex(index, entry);
        }
        return this;
    }

    public TabLines addAfter(int x, int y, String entry) {
        int index = getFirstEmptyIndex(getPosition(x, y));

        if (index != -1) {
            setLineByIndex(index, entry);
        }
        return this;
    }

    public TabLines left(int row, String entry) {
        set(1, row, entry);
        return this;
    }

    public TabLines middle(int row, String entry) {
        set(2, row, entry);
        return this;
    }

    public TabLines right(int row, String entry) {
        set(3, row, entry);
        return this;
    }

    public TabLines last(int row, String entry) {
        set(4, row, entry);
        return this;
    }

    public String[] build() {
        return lines;
    }
}
