package me.noeffort.alwaysinventory.event;

import me.noeffort.alwaysinventory.player.PlaceholderPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClosePlayerInventoryEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player closer;
    private PlaceholderPlayer player;

    public ClosePlayerInventoryEvent(Player closer, PlaceholderPlayer player) {
        this.closer = closer;
        this.player = player;
    }

    public Player getCloser() {
        return this.closer;
    }

    public PlaceholderPlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(PlaceholderPlayer player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
