package me.noeffort.alwaysinventory;

import org.bukkit.ChatColor;

public class Format {

    public static String format(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String format(String input, Object... objects) {
        return ChatColor.translateAlternateColorCodes('&', String.format(input, objects));
    }

}
