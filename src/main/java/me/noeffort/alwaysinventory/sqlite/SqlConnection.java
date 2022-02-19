package me.noeffort.alwaysinventory.sqlite;

import me.noeffort.alwaysinventory.AlwaysInventory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlConnection implements Closeable {

    private Connection connection;
    private File file;

    public SqlConnection(String name) throws IOException {
        if(!AlwaysInventory.validate())
            throw new IOException("Data folder does not exist!");
        try {
            Class.forName("org.sqlite.JDBC");
            this.file = new File(AlwaysInventory.getInstance().getDataFolder(), name);
            if(!this.file.exists()) {
                if(!this.file.createNewFile())
                    throw new IOException("Data file cannot be created!");
                AlwaysInventory.getInstance().getLogger().info("Data file created!");
            }
            this.connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", this.file.getAbsolutePath()));
        } catch (ClassNotFoundException ignore) {
            AlwaysInventory.getInstance().getLogger().severe("Could not load SQLite!");
            AlwaysInventory.getInstance().getServer().getPluginManager().disablePlugin(AlwaysInventory.getInstance());
        } catch (SQLException ignore) {
            AlwaysInventory.getInstance().getLogger().severe("Could not connect to SQLite database!");
            AlwaysInventory.getInstance().getServer().getPluginManager().disablePlugin(AlwaysInventory.getInstance());
        }
    }

    public void executeUpdate(String query) {
        try(Statement statement = this.connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void executeUpdate(String query, Object... args) {
        try(PreparedStatement statement = this.connection.prepareStatement(query)) {
            for(int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<List<Object>> executeQuery(String query, int outputs) {
        List<List<Object>> objects = new ArrayList<>();
        try(Statement statement = this.connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            int index = 0;
            while(result.next()) {
                List<Object> output = new ArrayList<>();
                for(int i = 0; i < outputs; i++) {
                    output.add(result.getObject(i + 1));
                }
                objects.add(index, output);
                index++;
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return objects;
    }

    public List<List<Object>> executeQuery(String query, int outputs, Object... args) {
        List<List<Object>> objects = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(query)) {
            for(int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            ResultSet result = statement.executeQuery();
            int index = 0;
            while(result.next()) {
                List<Object> output = new ArrayList<>();
                for(int i = 0; i < outputs; i++) {
                    output.add(result.getObject(i + 1));
                }
                objects.add(index, output);
                index++;
            }
            result.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return objects;
    }

    @Override
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            AlwaysInventory.getInstance().getLogger().severe("Could not close the SQLite connection!");
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public File getFile() {
        return this.file;
    }

}
