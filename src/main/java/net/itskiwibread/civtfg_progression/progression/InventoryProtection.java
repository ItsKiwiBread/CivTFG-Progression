package net.itskiwibread.civtfg_progression.progression;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber
public class InventoryProtection {

    /*
     * ============================================================
     * TEST / SAFETY SETTING
     * ============================================================
     */

    public static boolean DROP_FORBIDDEN_ITEMS = true;


    /*
     * ============================================================
     * SAFETY SCAN
     * ============================================================
     *
     * 40–80 seconds.
     */
    private static final int MIN_SCAN_DELAY = 20 * 10;
    private static final int MAX_SCAN_DELAY = 20 * 30;


    /*
     * Player UUID -> next safety scan
     */
    private static final Map<UUID, Long> NEXT_SCAN =
            new HashMap<>();


    /*
     * Players needing an inventory check.
     *
     * A Set is preferable to Map<UUID, Boolean> because we only
     * care whether the player is dirty or not.
     */
    private static final Set<UUID> DIRTY_PLAYERS =
            new HashSet<>();


    private static final Random RANDOM =
            new Random();


    // ============================================================
    // MARK DIRTY
    // ============================================================

    public static void markDirty(ServerPlayer player) {

        DIRTY_PLAYERS.add(
                player.getUUID()
        );
    }


    // ============================================================
    // PLAYER EVENTS
    // ============================================================

    @SubscribeEvent
    public static void onPickup(
            PlayerEvent.ItemPickupEvent event
    ) {

        if (event.getEntity() instanceof ServerPlayer player) {
            markDirty(player);
        }
    }


    @SubscribeEvent
    public static void onCraft(
            PlayerEvent.ItemCraftedEvent event
    ) {

        if (event.getEntity() instanceof ServerPlayer player) {
            markDirty(player);
        }
    }


    @SubscribeEvent
    public static void onSmelt(
            PlayerEvent.ItemSmeltedEvent event
    ) {

        if (event.getEntity() instanceof ServerPlayer player) {
            markDirty(player);
        }
    }


    /*
     * Clean up all cached data when the player leaves.
     */
    @SubscribeEvent
    public static void onPlayerLogout(
            PlayerEvent.PlayerLoggedOutEvent event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        UUID uuid =
                player.getUUID();

        DIRTY_PLAYERS.remove(uuid);
        NEXT_SCAN.remove(uuid);

        ProgressionMessage.removePlayer(player);
    }


    // ============================================================
    // PLAYER TICK
    // ============================================================

    @SubscribeEvent
    public static void onPlayerTick(
            TickEvent.PlayerTickEvent event
    ) {

        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }

        UUID uuid =
                player.getUUID();

        long gameTime =
                player.serverLevel().getGameTime();


        /*
         * Initialize the player's randomized safety timer.
         */
        if (!NEXT_SCAN.containsKey(uuid)) {

            scheduleNextScan(
                    uuid,
                    gameTime
            );
        }


        /*
         * ========================================================
         * EVENT-DRIVEN CHECK
         * ========================================================
         *
         * Only scan when something actually marked the inventory
         * dirty.
         */
        if (DIRTY_PLAYERS.remove(uuid)) {

            checkInventory(player);
        }


        /*
         * ========================================================
         * PERIODIC SAFETY CHECK
         * ========================================================
         */

        Long nextScan =
                NEXT_SCAN.get(uuid);

        if (nextScan != null
                && gameTime >= nextScan) {

            checkInventory(player);

            scheduleNextScan(
                    uuid,
                    gameTime
            );
        }
    }


    // ============================================================
    // INVENTORY CHECK
    // ============================================================

    private static void checkInventory(
            ServerPlayer player
    ) {

        if (!DROP_FORBIDDEN_ITEMS) {
            return;
        }

        ProgressionManager progression =
                ProgressionManager.get(
                        player.getServer()
                );


        /*
         * ========================================================
         * PLAYER INVENTORY
         * ========================================================
         */

        for (int slot = 0;
             slot < player.getInventory().getContainerSize();
             slot++) {

            ItemStack stack =
                    player.getInventory().getItem(slot);

            if (stack.isEmpty()) {
                continue;
            }

            int requiredGoal =
                    ProgressionRules.requiredGoal(stack);

            if (requiredGoal == -1) {
                continue;
            }

            if (progression.hasGoal(
                    player,
                    requiredGoal
            )) {
                continue;
            }


            /*
             * Forbidden item found.
             */
            ItemStack forbidden =
                    stack.copy();

            player.getInventory().setItem(
                    slot,
                    ItemStack.EMPTY
            );

            player.drop(
                    forbidden,
                    false
            );
        }


        /*
         * ========================================================
         * CURSOR / CARRIED ITEM
         * ========================================================
         */

        ItemStack carried =
                player.containerMenu.getCarried();

        if (!carried.isEmpty()) {

            int requiredGoal =
                    ProgressionRules.requiredGoal(
                            carried
                    );

            if (requiredGoal != -1
                    && !progression.hasGoal(
                    player,
                    requiredGoal
            )) {

                ItemStack forbidden =
                        carried.copy();

                player.containerMenu.setCarried(
                        ItemStack.EMPTY
                );

                player.drop(
                        forbidden,
                        false
                );
            }
        }


        player.inventoryMenu.broadcastChanges();
    }


    // ============================================================
    // RANDOM TIMER
    // ============================================================

    private static void scheduleNextScan(
            UUID uuid,
            long currentTime
    ) {

        int delay =
                MIN_SCAN_DELAY
                        + RANDOM.nextInt(
                        MAX_SCAN_DELAY
                                - MIN_SCAN_DELAY
                                + 1
                );

        NEXT_SCAN.put(
                uuid,
                currentTime + delay
        );
    }
}