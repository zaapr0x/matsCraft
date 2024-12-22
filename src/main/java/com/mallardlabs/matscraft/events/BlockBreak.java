package com.mallardlabs.matscraft.events;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp; // Import Timestamp
import java.time.Instant;
import java.security.MessageDigest; // Import untuk hashing
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import com.mallardlabs.matscraft.config.ConfigManager;

import java.util.List;
import java.util.ArrayList;

public class BlockBreak {

    private static final List<BlockBreakData> blockBreakDataList = new ArrayList<>();

    // Register the event
    public static void register() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (world instanceof ServerWorld serverWorld) {
                String blockType = state.getBlock().getTranslationKey();
                if (isTrackedBlock(blockType)) {
                    handleBlockBreak(serverWorld, player.getUuidAsString(), blockType, pos);
                }
            }
            return true; // Allow block break
        });
    }

    // Check if the block is tracked
    private static boolean isTrackedBlock(String blockType) {
        return switch (blockType) {
            case "block.matscraft.common_mats_ore",
                 "block.matscraft.epic_mats_ore",
                 "block.matscraft.legendary_mats_ore",
                 "block.matscraft.rare_mats_ore",
                 "block.matscraft.uncommon_mats_ore" -> true;
            default -> false;
        };
    }

    // Handle the block break event
    private static void handleBlockBreak(ServerWorld world, String playerUuid, String blockType, BlockPos pos) {
        blockBreakDataList.add(new BlockBreakData(playerUuid,blockType.replace("block.matscraft.", "") , pos, Instant.now()));

        // Optionally, check if batch size exceeds a limit and perform the batch insert
        if (blockBreakDataList.size() >= 100) {  // Example: batch size of 100
            insertBlockBreakBatch();
            blockBreakDataList.clear(); // Clear list after batch insert
        }
    }

    // Insert or update data in PostgreSQL using batching
    private static void insertBlockBreakBatch() {
        try (Connection conn = DriverManager.getConnection(ConfigManager.PG_URL, ConfigManager.PG_USER, ConfigManager.PG_PW)) {
            System.out.println("Koneksi ke PostgreSQL Supabase berhasil!");

            // Prepare the SQL query
            String query = "INSERT INTO minecraft_blocks (hash, minecraft_id, block, position, mined_at) "
                    + "VALUES (?, ?, ?, ?, ?) "
                    + "ON CONFLICT (hash) DO UPDATE "
                    + "SET mined_at = EXCLUDED.mined_at";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                // Loop through all the batched data and add them to the PreparedStatement
                for (BlockBreakData data : blockBreakDataList) {
                    String positionJson = String.format("{\"x\": %d, \"y\": %d, \"z\": %d}", data.pos.getX(), data.pos.getY(), data.pos.getZ());
                    Timestamp timestamp = Timestamp.from(data.minedAt);  // Convert Instant to Timestamp
                    String hash = generateHash(data.playerUuid, data.blockType, positionJson, timestamp.toString());

                    // Set the parameters for the current data
                    stmt.setString(1, hash);
                    stmt.setString(2, data.playerUuid);
                    stmt.setString(3, data.blockType);
                    stmt.setObject(4, positionJson, java.sql.Types.OTHER);  // Position as JSONB
                    stmt.setTimestamp(5, timestamp);

                    // Add the current statement to the batch
                    stmt.addBatch();
                }

                // Execute the batch
                int[] affectedRows = stmt.executeBatch();
                System.out.println("Batch Insert executed: " + affectedRows.length + " rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Generate hash using SHA-256 from minecraft_id, block, position, and mined_at
    private static String generateHash(String minecraftId, String blockType, String positionJson, String minedAt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String input = minecraftId + blockType + positionJson + minedAt;
            byte[] hashBytes = digest.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes); // Encode hash as Base64 string
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // Data container for block break events
    private static class BlockBreakData {
        String playerUuid;
        String blockType;
        BlockPos pos;
        Instant minedAt;

        public BlockBreakData(String playerUuid, String blockType, BlockPos pos, Instant minedAt) {
            this.playerUuid = playerUuid;
            this.blockType = blockType;
            this.pos = pos;
            this.minedAt = minedAt;
        }
    }
}
