package me.noeffort.alwaysinventory.command;

import me.noeffort.alwaysinventory.AlwaysInventory;
import me.noeffort.alwaysinventory.Constants;
import me.noeffort.alwaysinventory.Format;
import me.noeffort.alwaysinventory.event.OpenPlayerInventoryEvent;
import me.noeffort.alwaysinventory.inventory.InventorySerialization;
import me.noeffort.alwaysinventory.player.PlaceholderPlayer;
import me.noeffort.alwaysinventory.sqlite.SqlConnection;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PeekCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if(!player.hasPermission("alwaysinventory.peek")) {
            player.sendMessage(Format.format("&cYou do not have permission to perform this command!"));
            return true;
        }

        if(args.length < 1) {
            player.sendMessage(Format.format("&cYou must specify a player!"));
            return true;
        }

        if(player.getName().equalsIgnoreCase(args[0])) {
            player.sendMessage(Format.format("&cYou cannot peek into your own inventory!"));
            return true;
        }

        Map<String, Player> players = AlwaysInventory.getInstance().getServer().getOnlinePlayers().stream()
                .collect(Collectors.toMap(k -> k.getName().toLowerCase(), v -> v));
        if(players.containsKey(args[0].toLowerCase())) {
            Player p = players.get(args[0].toLowerCase());
            OpenPlayerInventoryEvent event = new OpenPlayerInventoryEvent(player,
                    new PlaceholderPlayer(p.getUniqueId(), p.getName()));
            Bukkit.getPluginManager().callEvent(event);
            if(event.isCancelled()) return true;
            // We know this player is online
            player.openInventory(((Player) event.getPlayer()).getInventory());
        } else {
            try(SqlConnection connection = new SqlConnection(Constants.CONNECTION)) {
                List<PlaceholderPlayer> objects = this.getPlayerData(connection, args[0]);
                if(objects == null) {
                    player.sendMessage(Format.format("&cPlayer specified does not exist!"));
                    return true;
                }
                OpenPlayerInventoryEvent event = new OpenPlayerInventoryEvent(player,
                        new PlaceholderPlayer(objects.get(0).getUniqueId(), args[0]));
                Bukkit.getPluginManager().callEvent(event);
                if(event.isCancelled()) return true;
                if(!event.getPlayer().getName().equalsIgnoreCase(objects.get(0).getName())) {
                    objects = this.getPlayerData(connection, event.getPlayer().getName());
                    if(objects == null) {
                        player.sendMessage(Format.format("&c[Redirect] Player specified does not exist!"));
                        return true;
                    }
                }
                // We know this player is offline
                player.openInventory(InventorySerialization.deserialize(objects.get(0)));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    /*
     * SQL QUERY LEGEND:
     * objects.get(N)           -> N'th player in list
     * objects.get(N).get(0)    -> N'th player's UUID
     * objects.get(N).get(1)    -> N'th player's Name
     * objects.get(N).get(2)    -> N'th player's Inventory Data
     */
    private List<PlaceholderPlayer> getPlayerData(SqlConnection connection, String name) {
        List<List<Object>> objects = connection.executeQuery("select uuid, name, inventory from users where name = ?",
                3, name.toLowerCase());
        if(objects.size() == 0) return null;
        return objects.stream().map(x -> new PlaceholderPlayer(UUID.fromString(String.valueOf(x.get(0))),
                String.valueOf(x.get(1)), String.valueOf(x.get(2)))).collect(Collectors.toList());
    }

}
