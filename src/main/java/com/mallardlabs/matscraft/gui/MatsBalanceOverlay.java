package com.mallardlabs.matscraft.gui;

import com.mallardlabs.matscraft.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MatsBalanceOverlay implements ModInitializer {
    // Static variable to store player balance globally
    public static int playerBalance = 0;

    // Flag to detect if it's the player's first spawn
    private boolean isFirstSpawn = true;

    @Override
    public void onInitialize() {
        // Register the HUD render callback to display the balance
        HudRenderCallback.EVENT.register(this::renderMatsBalance);

        // Listen for the client tick event to detect when the player spawns
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && isFirstSpawn) {
                isFirstSpawn = false;

                // Sync the account by fetching balance from the database
                syncAccount(client.player.getUuidAsString());
            }
        });
    }

    // Method to render the HUD overlay
    private void renderMatsBalance(DrawContext context, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        if (player != null && !isFirstSpawn) {
            // Render the player's current Mats balance
            int totalMats = playerBalance;

            TextRenderer textRenderer = client.textRenderer;

            int x = 10; // X position of the overlay
            int y = 10; // Y position of the overlay
            int width = 80; // Width of the background
            int height = 20; // Height of the background

            // Draw a semi-transparent background
            context.fill(x - 5, y - 5, x + width, y + height, 0x80000000);

            // Render "Your Balance" text
            context.drawText(
                    textRenderer,
                    "Your Balance",
                    x, y,
                    0xFFFFFF, // White color
                    true // Enable shadow
            );

            // Render the player's Mats balance
            context.drawText(
                    textRenderer,
                    totalMats + " Mats",
                    x, y + 10, // Offset by 10 pixels below "Your Balance"
                    0xFFFF00, // Yellow color
                    true // Enable shadow
            );
        }
    }

    /**
     * Sync the player's account and get the balance from the database.
     */
    private void syncAccount(String playerUuid) {
        // Database connection details from ConfigManager
        String dbUrl = ConfigManager.PG_URL;
        String dbUser = ConfigManager.PG_USER;
        String dbPassword = ConfigManager.PG_PW;

        // SQL query to check if the player's UUID exists in the users table
        String userCheckQuery = "SELECT minecraft_id FROM users WHERE minecraft_id = ?";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // Check if the player's UUID exists in the users table
            try (PreparedStatement stmt = conn.prepareStatement(userCheckQuery)) {
                stmt.setString(1, playerUuid); // Set the player's UUID

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Player found in users table, now fetch their balance
                        updateBalanceFromDatabase(playerUuid, conn);
                    } else {
                        // Player not found, inform the player
                        MinecraftClient.getInstance().player.sendMessage(
                                Text.literal("Sync Failed, Account Not Linked")
                                        .formatted(Formatting.RED), // Red color for error
                                false
                        );
                    }
                }
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            MinecraftClient.getInstance().player.sendMessage(
                    Text.literal("Error syncing account: " + e.getMessage())
                            .formatted(Formatting.RED), // Red color for error
                    false
            );
        }
    }

    /**
     * Fetch the player's balance from the mats_balance table and update the global balance.
     */
    private void updateBalanceFromDatabase(String playerUuid, Connection conn) {
        // SQL query to get the balance for the player's Minecraft UUID from mats_balance
        String query = "SELECT balance FROM mats_balance WHERE minecraft_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, playerUuid); // Set the player's UUID

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Get the balance from the result set
                    int balance = rs.getInt("balance");

                    // Update the player balance in MatsBalanceOverlay
                    playerBalance = balance;

                    // Optionally, send a message to the player indicating the balance
                    MinecraftClient.getInstance().player.sendMessage(
                            Text.literal("Your balance has been updated: " + balance)
                                    .formatted(Formatting.GREEN), // Green color for success
                            false
                    );

                    // Send a welcome back message
                    MinecraftClient.getInstance().player.sendMessage(
                            Text.literal("Welcome back, " + MinecraftClient.getInstance().player.getName().getString() + "!")
                                    .formatted(Formatting.GOLD), // Gold color for success
                            false
                    );
                } else {
                    // If no balance is found, inform the player
                    MinecraftClient.getInstance().player.sendMessage(
                            Text.literal("No balance found for your account.")
                                    .formatted(Formatting.RED), // Red color for error
                            false
                    );
                }
            }
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            MinecraftClient.getInstance().player.sendMessage(
                    Text.literal("Error fetching balance from the database.")
                            .formatted(Formatting.RED), // Red color for error
                    false
            );
        }
    }
}
