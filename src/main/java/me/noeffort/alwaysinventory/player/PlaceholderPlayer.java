package me.noeffort.alwaysinventory.player;

import java.util.UUID;

public class PlaceholderPlayer {

    private final UUID uuid;
    private final String name;
    private String data;

    public PlaceholderPlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public PlaceholderPlayer(UUID uuid, String name, String data) {
        this(uuid, name);
        this.data = data;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getData() {
        return this.data;
    }

    public PlaceholderPlayer setData(String data) {
        this.data = data;
        return this;
    }
}
