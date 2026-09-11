package net.itskiwibread.civtfg_progression.progression;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ItemUseProtection {

    /*
     * ============================================================
     * RIGHT CLICK ITEM
     * ============================================================
     */

    @SubscribeEvent
    public static void onRightClickItem(
            PlayerInteractEvent.RightClickItem event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (isLocked(player, event.getItemStack())) {

            event.setCanceled(true);

            player.sendSystemMessage(
                    Component.literal(
                            ProgressionRules.lockedUseMessage(
                                    requiredGoal(event.getItemStack())
                            )
                    )
            );
        }
    }


    /*
     * ============================================================
     * RIGHT CLICK BLOCK
     * ============================================================
     *
     * This catches things such as:
     *
     * right-clicking a block with a locked item
     */
    @SubscribeEvent
    public static void onRightClickBlock(
            PlayerInteractEvent.RightClickBlock event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack stack =
                event.getItemStack();

        if (isLocked(player, stack)) {

            event.setCanceled(true);

            ProgressionMessage.sendUseBlocked(
                    player,
                    stack,
                    requiredGoal(stack)
            );
        }
    }


    /*
     * ============================================================
     * RIGHT CLICK ENTITY
     * ============================================================
     */

    @SubscribeEvent
    public static void onEntityInteract(
            PlayerInteractEvent.EntityInteract event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack stack =
                event.getItemStack();

        if (isLocked(player, stack)) {

            event.setCanceled(true);

            player.sendSystemMessage(
                    Component.literal(
                            ProgressionRules.lockedUseMessage(
                                    requiredGoal(stack)
                            )
                    )
            );
        }
    }


    /*
     * ============================================================
     * RIGHT CLICK ENTITY - SPECIFIC
     * ============================================================
     *
     * Some entity interactions use EntityInteractSpecific first.
     */
    @SubscribeEvent
    public static void onEntityInteractSpecific(
            PlayerInteractEvent.EntityInteractSpecific event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack stack =
                event.getItemStack();

        if (isLocked(player, stack)) {

            event.setCanceled(true);

            player.sendSystemMessage(
                    Component.literal(
                            ProgressionRules.lockedUseMessage(
                                    requiredGoal(stack)
                            )
                    )
            );
        }
    }


    /*
     * ============================================================
     * ATTACK ENTITY
     * ============================================================
     *
     * Prevents using a locked tool as a weapon.
     */
    @SubscribeEvent
    public static void onAttackEntity(
            AttackEntityEvent event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack stack =
                player.getItemInHand(InteractionHand.MAIN_HAND);

        if (isLocked(player, stack)) {

            event.setCanceled(true);

            player.sendSystemMessage(
                    Component.literal(
                            ProgressionRules.lockedUseMessage(
                                    requiredGoal(stack)
                            )
                    )
            );
        }
    }


    /*
     * ============================================================
     * HELPER
     * ============================================================
     */

    private static boolean isLocked(
            ServerPlayer player,
            ItemStack stack
    ) {

        int goal =
                requiredGoal(stack);

        if (goal == -1) {
            return false;
        }

        ProgressionManager progression =
                ProgressionManager.get(player.getServer());

        return !progression.hasGoal(
                player,
                goal
        );
    }


    private static int requiredGoal(ItemStack stack) {
        return ProgressionRules.requiredGoal(stack);
    }
}