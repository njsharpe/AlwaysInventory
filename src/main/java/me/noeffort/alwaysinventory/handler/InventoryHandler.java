package me.noeffort.alwaysinventory.handler;

import me.noeffort.alwaysinventory.Constants;
import me.noeffort.alwaysinventory.event.ClosePlayerInventoryEvent;
import me.noeffort.alwaysinventory.event.OpenPlayerInventoryEvent;
import me.noeffort.alwaysinventory.inventory.InventorySerialization;
import me.noeffort.alwaysinventory.inventory.PeekInventoryHolder;
import me.noeffort.alwaysinventory.player.PlaceholderPlayer;
import me.noeffort.alwaysinventory.sqlite.SqlConnection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.io.IOException;

public class InventoryHandler implements Listener {

    @EventHandler
    public void onOpen(OpenPlayerInventoryEvent event) {
        // Do whatever we want to do when opening an inventory here...
    }

    @EventHandler
    public void onClose(ClosePlayerInventoryEvent event) {
        // Do whatever we want to do when closing an inventory here...
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(event.getInventory().getHolder() == null || !(event.getInventory().getHolder() instanceof PeekInventoryHolder)) return;
        PeekInventoryHolder holder = (PeekInventoryHolder) event.getInventory().getHolder();
        ClosePlayerInventoryEvent e = new ClosePlayerInventoryEvent(((Player) event.getPlayer()), holder.getPlayer());
        Bukkit.getPluginManager().callEvent(e);
        try(SqlConnection connection = new SqlConnection(Constants.CONNECTION)) {
            PlaceholderPlayer player = holder.getPlayer();
            connection.executeUpdate("insert into " + Constants.TABLE + " (uuid, name, inventory) values (?, ?, ?)"
                            + " on conflict (name) do update set inventory = excluded.inventory",
                    player.getUniqueId().toString(), player.getName().toLowerCase(), InventorySerialization.serialize(event.getInventory()).trim());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
