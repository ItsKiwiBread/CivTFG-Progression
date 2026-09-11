package net.itskiwibread.civtfg_progression.progression;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ProgressionMessage {

    private ProgressionMessage() {
    }

    /*
     * Player UUID -> Item ID -> next time this message may appear.
     */
    private static final Map<UUID, Map<String, Long>> COOLDOWNS =
            new HashMap<>();

    /*
     * One second between identical warnings.
     */
    private static final long COOLDOWN_TICKS = 20;


    public static void sendPickupBlocked(
            ServerPlayer player,
            ItemStack stack,
            int goal
    ) {

        if (!canSend(player, stack)) {
            return;
        }

        player.sendSystemMessage(
                Component.literal(
                        ProgressionRules.lockedPickupMessage(goal)
                )
        );
    }


    public static void sendUseBlocked(
            ServerPlayer player,
            ItemStack stack,
            int goal
    ) {

        if (!canSend(player, stack)) {
            return;
        }

        player.sendSystemMessage(
                Component.literal(
                        ProgressionRules.lockedUseMessage(goal)
                )
        );
    }


    private static boolean canSend(
            ServerPlayer player,
            ItemStack stack
    ) {

        long gameTime =
                player.serverLevel().getGameTime();

        UUID playerId =
                player.getUUID();

        String itemId =
                net.minecraft.core.registries.BuiltInRegistries.ITEM
                        .getKey(stack.getItem())
                        .toString();

        Map<String, Long> playerCooldowns =
                COOLDOWNS.computeIfAbsent(
                        playerId,
                        ignored -> new HashMap<>()
                );

        long nextAllowed =
                playerCooldowns.getOrDefault(
                        itemId,
                        0L
                );

        if (gameTime < nextAllowed) {
            return false;
        }

        playerCooldowns.put(
                itemId,
                gameTime + COOLDOWN_TICKS
        );

        return true;
    }


    /*
     * Remove all cached data for a player who left.
     */
    public static void removePlayer(ServerPlayer player) {

        COOLDOWNS.remove(
                player.getUUID()
        );
    }
}