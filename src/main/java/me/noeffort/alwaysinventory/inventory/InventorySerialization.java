package me.noeffort.alwaysinventory.inventory;

import me.noeffort.alwaysinventory.player.PlaceholderPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InventorySerialization {

    public static String serialize(Inventory inventory) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try(BukkitObjectOutputStream output = new BukkitObjectOutputStream(stream)) {
            ItemStack[] items = inventory.getContents();
            output.writeInt(items.length);
            for(ItemStack item : items) {
                output.writeObject(item);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Base64Coder.encodeLines(stream.toByteArray());
    }

    public static Inventory deserialize(PlaceholderPlayer player) {
        Inventory inventory = Bukkit.createInventory(new PeekInventoryHolder(player), InventoryType.PLAYER, "Player");
        ByteArrayInputStream stream = new ByteArrayInputStream(Base64Coder.decodeLines(player.getData()));
        try(BukkitObjectInputStream input = new BukkitObjectInputStream(stream)) {
            ItemStack[] items = new ItemStack[input.readInt()];
            for(int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) input.readObject();
            }
            inventory.setContents(items);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ignore) {}
        return inventory;
    }

}
