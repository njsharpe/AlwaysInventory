package me.noeffort.alwaysinventory.inventory;

import me.noeffort.alwaysinventory.player.PlaceholderPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class PeekInventoryHolder implements InventoryHolder {

    private final PlaceholderPlayer player;

    public PeekInventoryHolder(PlaceholderPlayer player) {
        this.player = player;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public PlaceholderPlayer getPlayer() {
        return this.player;
    }
}
