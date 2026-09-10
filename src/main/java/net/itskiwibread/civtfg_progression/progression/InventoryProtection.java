package net.itskiwibread.civtfg_progression.progression;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class InventoryProtection {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        /*
         * Only check once per player tick, and only at the END
         * of the tick after other mods/game logic had a chance
         * to modify the inventory.
         */
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (!(event.player instanceof ServerPlayer player)) {
            return;
        }

        /*
         * IMPORTANT:
         *
         * Turn this off when you want to test what happens when
         * a forbidden item is allowed to remain in the inventory.
         */
        if (!ProgressionRules.DROP_FORBIDDEN_ITEMS) {
            return;
        }

        ProgressionManager progression =
                ProgressionManager.get(player.getServer());

        /*
         * Check the player's normal inventory.
         *
         * We deliberately check the entire player inventory rather
         * than only the hotbar.
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

            /*
             * Not one of our progression-locked test items.
             */
            if (requiredGoal == -1) {
                continue;
            }

            /*
             * The player is allowed to have this item.
             */
            if (progression.hasGoal(player, requiredGoal)) {
                continue;
            }

            /*
             * --------------------------------------------------------
             * FORBIDDEN ITEM FOUND
             * --------------------------------------------------------
             */

            ItemStack forbidden = stack.copy();

            /*
             * Remove it from the player's inventory BEFORE spawning
             * the replacement ItemEntity.
             */
            player.getInventory().setItem(
                    slot,
                    ItemStack.EMPTY
            );

            /*
             * Drop the item immediately.
             *
             * The first argument 'false' means it isn't treated
             * as a normal player drop in the same way as Q.
             */
            player.drop(
                    forbidden,
                    false
            );

            /*
             * Prevent the same stack from being processed repeatedly.
             */
            player.inventoryMenu.broadcastChanges();
        }
    }
}