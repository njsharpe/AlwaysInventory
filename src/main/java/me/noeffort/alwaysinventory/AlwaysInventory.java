package me.noeffort.alwaysinventory;

import me.noeffort.alwaysinventory.command.AlwaysInventoryCommand;
import me.noeffort.alwaysinventory.command.PeekCommand;
import me.noeffort.alwaysinventory.handler.InventoryHandler;
import me.noeffort.alwaysinventory.handler.PlayerHandler;
import me.noeffort.alwaysinventory.sqlite.SqlConnection;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AlwaysInventory extends JavaPlugin {

    private static AlwaysInventory instance;
    private PlayerHandler handler;

    @Override
    public void onEnable() {
        instance = this;
        this.handler = new PlayerHandler();

        this.getServer().getPluginManager().registerEvents(this.handler, this);
        this.getServer().getPluginManager().registerEvents(new InventoryHandler(), this);
        Objects.requireNonNull(this.getCommand("alwaysinventory")).setExecutor(new AlwaysInventoryCommand());
        Objects.requireNonNull(this.getCommand("peek")).setExecutor(new PeekCommand());

        try(SqlConnection connection = new SqlConnection(Constants.CONNECTION)) {
            connection.executeUpdate("create table if not exists " + Constants.TABLE + " ( " +
                    "uuid text primary key, name text not null unique, inventory text not null );");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        AlwaysInventory.getInstance().getServer().getOnlinePlayers().forEach(player -> {
            PlayerJoinEvent event = new PlayerJoinEvent(player, "");
            AlwaysInventory.getInstance().getPlayerHandler().onPlayerJoin(event);
        });
    }

    @Override
    public void onDisable() {
        AlwaysInventory.getInstance().getServer().getOnlinePlayers().forEach(player -> {
            PlayerQuitEvent event = new PlayerQuitEvent(player, "");
            AlwaysInventory.getInstance().getPlayerHandler().onPlayerQuit(event);
        });

        instance = null;
        this.handler = null;
    }

    public static AlwaysInventory getInstance() {
        return instance;
    }

    public PlayerHandler getPlayerHandler() {
        return this.handler;
    }

    public static boolean validate() {
        File folder = instance.getDataFolder();
        if(!folder.exists()) return folder.mkdirs();
        return true;
    }

}
