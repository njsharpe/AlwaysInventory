package me.noeffort.alwaysinventory.command;

import me.noeffort.alwaysinventory.AlwaysInventory;
import me.noeffort.alwaysinventory.Format;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.PlayerJoinEvent;

public class AlwaysInventoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("alwaysinventory.use")) {
            sender.sendMessage(Format.format("&cYou do not have permission to perform this command!"));
            return true;
        }
        AlwaysInventory.getInstance().getServer().getOnlinePlayers().forEach(player -> {
            PlayerJoinEvent event = new PlayerJoinEvent(player, "");
            AlwaysInventory.getInstance().getPlayerHandler().onPlayerJoin(event);
        });
        sender.sendMessage(Format.format("&aAlwaysInventory has been reloaded!"));
        return true;
    }

}
