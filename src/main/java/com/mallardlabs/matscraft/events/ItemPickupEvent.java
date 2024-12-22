package com.mallardlabs.matscraft.events;

import com.mallardlabs.matscraft.config.ConfigManager;
import com.mallardlabs.matscraft.gui.MatsBalanceOverlay; // Import MatsBalanceOverlay to update balance
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemPickupEvent {

    // Daftarkan event untuk memantau inventori pemain pada setiap tick
    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                if (player != null) {
                    checkAndRemoveMats(player);
                }
            });
        });
    }

    // Periksa inventori pemain untuk item "mats" dan hapus jika ditemukan
    private static void checkAndRemoveMats(ServerPlayerEntity player) {
        var inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.getItem().getTranslationKey().equals("item.matscraft.mats")) {
                int count = stack.getCount(); // Ambil jumlah item dalam stack

                // Hapus item dari slot inventory
                inventory.removeStack(i);

                // Update balance in database
                updateBalanceInDatabase(player, count);

                // Tampilkan pesan ke pemain
                player.sendMessage(
                        Text.literal("+ " + count + " Mats") // Format pesan
                                .formatted(Formatting.GREEN), // Warna hijau
                        true
                );
            }
        }
    }

    /**
     * Update the player's balance in the mats_balance table based on the count of Mats.
     */
    private static void updateBalanceInDatabase(ServerPlayerEntity player, int count) {
        String playerUuid = player.getUuidAsString(); // Get the player's UUID

        // Database connection details from ConfigManager
        String dbUrl = ConfigManager.PG_URL;
        String dbUser = ConfigManager.PG_USER;
        String dbPassword = ConfigManager.PG_PW;

        // SQL query to check if the player's UUID exists in the mats_balance table
        String userCheckQuery = "SELECT balance FROM mats_balance WHERE minecraft_id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // Check if the player is already in the mats_balance table
            try (PreparedStatement stmt = conn.prepareStatement(userCheckQuery)) {
                stmt.setString(1, playerUuid); // Set the player's UUID

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Player found, update balance by adding the count of Mats
                        int currentBalance = rs.getInt("balance");
                        int newBalance = currentBalance + count;

                        // Update the balance in the mats_balance table
                        updatePlayerBalance(playerUuid, newBalance, conn);

                        // Update the balance globally for MatsBalanceOverlay
                        MatsBalanceOverlay.playerBalance = newBalance;

                        // Send the updated balance message to the player
                        player.sendMessage(
                                Text.literal("Your new balance: " + newBalance + " Mats")
                                        .formatted(Formatting.GOLD),
                                true
                        );
                    } else {
                        // If player is not found, insert a new record with the Mats count
                        insertNewPlayerBalance(playerUuid, count, conn);

                        // Set the balance globally for MatsBalanceOverlay
                        MatsBalanceOverlay.playerBalance = count;

                        // Send the initial balance message to the player
                        player.sendMessage(
                                Text.literal("Sync Failed, Account Not Linked!")
                                        .formatted(Formatting.RED),
                                true
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage(
                    Text.literal("Error updating balance: " + e.getMessage())
                            .formatted(Formatting.RED),
                    false
            );
        }
    }

    /**
     * Update the player's balance in the mats_balance table.
     */
    private static void updatePlayerBalance(String playerUuid, int newBalance, Connection conn) throws SQLException {
        String updateQuery = "UPDATE mats_balance SET balance = ? WHERE minecraft_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setInt(1, newBalance); // Set the updated balance
            stmt.setString(2, playerUuid); // Set the player's UUID
            stmt.executeUpdate();
        }
    }

    /**
     * Insert a new player balance record in the mats_balance table.
     */
    private static void insertNewPlayerBalance(String playerUuid, int balance, Connection conn) throws SQLException {
        String insertQuery = "INSERT INTO mats_balance (minecraft_id, balance) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            stmt.setString(1, playerUuid); // Set the player's UUID
            stmt.setInt(2, balance); // Set the initial balance
            stmt.executeUpdate();
        }
    }
}
