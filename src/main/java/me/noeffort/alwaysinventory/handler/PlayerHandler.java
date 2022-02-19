package me.noeffort.alwaysinventory.handler;

import me.noeffort.alwaysinventory.Constants;
import me.noeffort.alwaysinventory.inventory.InventorySerialization;
import me.noeffort.alwaysinventory.sqlite.SqlConnection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class PlayerHandler implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try(SqlConnection connection = new SqlConnection(Constants.CONNECTION)) {
            connection.executeUpdate("insert into " + Constants.TABLE + " (uuid, name, inventory) values (?, ?, ?)"
                            + " on conflict (name) do update set inventory = excluded.inventory",
                    player.getUniqueId().toString(), player.getName().toLowerCase(), InventorySerialization.serialize(player.getInventory()).trim());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        try(SqlConnection connection = new SqlConnection(Constants.CONNECTION)) {
            connection.executeUpdate("insert into " + Constants.TABLE + " (uuid, name, inventory) values (?, ?, ?)"
                            + " on conflict (name) do update set inventory = excluded.inventory",
                    player.getUniqueId().toString(), player.getName().toLowerCase(), InventorySerialization.serialize(player.getInventory()).trim());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
