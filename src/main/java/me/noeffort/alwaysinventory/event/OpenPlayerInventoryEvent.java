package me.noeffort.alwaysinventory.event;

import me.noeffort.alwaysinventory.player.PlaceholderPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OpenPlayerInventoryEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Player opener;
    private PlaceholderPlayer player;
    private boolean isCancelled;

    public OpenPlayerInventoryEvent(Player opener, PlaceholderPlayer player) {
        this.opener = opener;
        this.player = player;
    }

    public Player getOpener() {
        return this.opener;
    }

    public PlaceholderPlayer getPlayer() {
        return this.player;
    }

    public void setPlayer(PlaceholderPlayer player) {
        this.player = player;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
