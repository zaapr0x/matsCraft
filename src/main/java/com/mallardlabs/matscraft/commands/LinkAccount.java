package com.mallardlabs.matscraft.commands;

import com.google.gson.Gson;
import com.mallardlabs.matscraft.config.ConfigManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class LinkAccount {

    private static final Gson gson = new Gson(); // Gson instance for JSON operations

    /**
     * Registers the "/link" command to the dispatcher.
     */
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("link")
                .then(CommandManager.argument("token", StringArgumentType.word())
                        .executes(LinkAccount::executeCommand)));
    }

    /**
     * Executes the /link command, verifies the token, and processes account linking.
     * @param context Command context that contains details about the execution.
     * @return Success or failure status.
     */
    private static int executeCommand(CommandContext<ServerCommandSource> context) {
        try {
            // Retrieve token, player name, and user ID from context
            String token = StringArgumentType.getString(context, "token");
            String playerName = context.getSource().getPlayer().getName().getString();
            String userId = context.getSource().getPlayer().getUuidAsString();

            // Inform player that token verification is in progress
            context.getSource().sendFeedback(() -> Text.of("Verifying Tokens... Please wait."), false);

            // Call method to verify token and update database if valid
            verifyAndLinkAccount(playerName, userId, token, context);

            return 1; // Command executed successfully
        } catch (Exception e) {
            // Send feedback if an error occurs during execution
            context.getSource().sendFeedback(() -> Text.of("Command Error: " + e.getMessage()), false);
            e.printStackTrace();
            return 0; // Command failed
        }
    }

    /**
     * Verifies the provided token against the database and links the account if valid.
     * @param playerName The player's Minecraft username.
     * @param userId The player's Minecraft UUID.
     * @param token The verification token.
     * @param context Command context that contains details about the execution.
     */
    private static void verifyAndLinkAccount(String playerName, String userId, String token, CommandContext<ServerCommandSource> context) {
        // Database connection details
        String dbUrl = ConfigManager.PG_URL;
        String dbUser = ConfigManager.PG_USER;
        String dbPassword = ConfigManager.PG_PW;

        // SQL query to check the token in auth_tokens table
        String tokenCheckQuery = "SELECT discord_id FROM auth_tokens WHERE verification_token = ? AND status = 'pending'";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // Prepare statement to verify the token and fetch discord_id
            try (PreparedStatement stmt = conn.prepareStatement(tokenCheckQuery)) {
                stmt.setString(1, token);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String discordId = rs.getString("discord_id"); // Retrieve discord_id from the result
                        // Token is valid and pending, proceed with account linking
                        updateUserAndTokenStatus(playerName, userId, discordId, token, conn, context);
                    } else {
                        // If token is invalid or expired, inform the player
                        context.getSource().sendFeedback(() -> Text.of("Invalid or expired token."), false);
                    }
                }
            }
        } catch (SQLException e) {
            // If an error occurs during the token verification process
            e.printStackTrace();
            context.getSource().sendFeedback(() -> Text.of("Error verifying the token: " + e.getMessage()), false);
        }
    }

    /**
     * Updates the user information and marks the token as used in the database.
     * Also inserts a new balance record for the user in mats_balance table.
     * @param playerName The player's Minecraft username.
     * @param userId The player's Minecraft UUID.
     * @param discordId The user's Discord ID retrieved from the auth_tokens table.
     * @param token The verification token.
     * @param conn The database connection object.
     * @param context Command context to send feedback to the player.
     */
    private static void updateUserAndTokenStatus(String playerName, String userId, String discordId, String token, Connection conn, CommandContext<ServerCommandSource> context) {
        // Convert discordId to a long if needed (assuming it can be parsed as long)
        long discordIdLong = Long.parseLong(discordId);

        // SQL queries for updating users and tokens, and inserting into mats_balance
        String updateUserQuery = "UPDATE users SET minecraft_username = ?, minecraft_id = ?, is_verified = TRUE, updated_at = ? WHERE discord_id = ?";
        String updateTokenQuery = "UPDATE auth_tokens SET status = 'used' WHERE verification_token = ?";
        String insertBalanceQuery = "INSERT INTO mats_balance (minecraft_id, balance, updated_at) VALUES (?, 0, ?)";

        try {
            // Start a transaction for atomic operations
            conn.setAutoCommit(false);

            // Update users table with new data
            try (PreparedStatement stmt = conn.prepareStatement(updateUserQuery)) {
                stmt.setString(1, playerName);  // Set minecraft_username
                stmt.setString(2, userId);      // Set minecraft_id
                stmt.setTimestamp(3, Timestamp.from(Instant.now()));  // Set updated_at
                stmt.setLong(4, discordIdLong);   // Set discord_id to match user

                stmt.executeUpdate();
            }

            // Update auth_tokens table to mark the token as 'used'
            try (PreparedStatement stmt = conn.prepareStatement(updateTokenQuery)) {
                stmt.setString(1, token);
                stmt.executeUpdate();
            }

            // Insert new balance entry into mats_balance table
            try (PreparedStatement stmt = conn.prepareStatement(insertBalanceQuery)) {
                stmt.setString(1, userId);  // Set minecraft_id
                stmt.setTimestamp(2, Timestamp.from(Instant.now()));  // Set updated_at
                stmt.executeUpdate();
            }

            // Commit the transaction after all operations are successful
            conn.commit();

            // Inform the player that the account was successfully linked
            context.getSource().sendFeedback(() -> Text.of("Account successfully linked!"), false);
        } catch (SQLException e) {
            // Rollback transaction in case of any error
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            context.getSource().sendFeedback(() -> Text.of("Error linking account: " + e.getMessage()), false);
        } finally {
            // Restore auto-commit mode
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
