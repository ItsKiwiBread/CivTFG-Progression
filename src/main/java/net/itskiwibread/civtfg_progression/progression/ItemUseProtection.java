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


    // ============================================================
    // RIGHT CLICK ITEM
    // ============================================================

    @SubscribeEvent
    public static void onRightClickItem(
            PlayerInteractEvent.RightClickItem event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        InteractionHand hand =
                event.getHand();

        if (blockUse(
                player,
                hand,
                event.getItemStack()
        )) {

            event.setCanceled(true);
        }
    }


    // ============================================================
    // RIGHT CLICK BLOCK
    // ============================================================

    @SubscribeEvent
    public static void onRightClickBlock(
            PlayerInteractEvent.RightClickBlock event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        InteractionHand hand =
                event.getHand();

        if (blockUse(
                player,
                hand,
                event.getItemStack()
        )) {

            event.setCanceled(true);
        }
    }


    // ============================================================
    // RIGHT CLICK ENTITY
    // ============================================================

    @SubscribeEvent
    public static void onEntityInteract(
            PlayerInteractEvent.EntityInteract event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        InteractionHand hand =
                event.getHand();

        if (blockUse(
                player,
                hand,
                event.getItemStack()
        )) {

            event.setCanceled(true);
        }
    }


    // ============================================================
    // SPECIFIC ENTITY INTERACTION
    // ============================================================

    @SubscribeEvent
    public static void onEntityInteractSpecific(
            PlayerInteractEvent.EntityInteractSpecific event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        InteractionHand hand =
                event.getHand();

        if (blockUse(
                player,
                hand,
                event.getItemStack()
        )) {

            event.setCanceled(true);
        }
    }


    // ============================================================
    // ATTACK ENTITY
    // ============================================================

    @SubscribeEvent
    public static void onAttackEntity(
            AttackEntityEvent event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        InteractionHand hand =
                InteractionHand.MAIN_HAND;

        ItemStack stack =
                player.getItemInHand(hand);

        if (!shouldBlock(
                player,
                stack
        )) {

            return;
        }

        int goal =
                ProgressionRules.requiredGoal(stack);

        ProgressionMessage.sendUseBlocked(
                player,
                stack,
                goal
        );

        dropAndCancel(
                player,
                hand,
                stack
        );

        event.setCanceled(true);
    }


    // ============================================================
    // COMMON RIGHT-CLICK LOGIC
    // ============================================================

    private static boolean blockUse(
            ServerPlayer player,
            InteractionHand hand,
            ItemStack stack
    ) {

        if (!shouldBlock(
                player,
                stack
        )) {

            return false;
        }

        int goal =
                ProgressionRules.requiredGoal(stack);

        ProgressionMessage.sendUseBlocked(
                player,
                stack,
                goal
        );

        dropAndCancel(
                player,
                hand,
                stack
        );

        return true;
    }


    // ============================================================
    // SHOULD BLOCK?
    // ============================================================

    private static boolean shouldBlock(
            ServerPlayer player,
            ItemStack stack
    ) {

        if (stack.isEmpty()) {
            return false;
        }

        /*
         * Item isn't configured to be use-blocked.
         */
        if (!ProgressionRules.shouldBlockUse(stack)) {
            return false;
        }

        int goal =
                ProgressionRules.requiredGoal(stack);

        if (goal == -1) {
            return false;
        }

        ProgressionManager progression =
                ProgressionManager.get(
                        player.getServer()
                );

        return !progression.hasGoal(
                player,
                goal
        );
    }


    // ============================================================
    // DROP ITEM
    // ============================================================

    private static void dropAndCancel(
            ServerPlayer player,
            InteractionHand hand,
            ItemStack currentStack
    ) {

        /*
         * The JSON lets you disable dropping independently.
         */
        if (!ProgressionRules
                .shouldDropOnBlockedUse(currentStack)) {

            return;
        }

        ItemStack dropped =
                currentStack.copy();

        /*
         * Drop the entire held stack.
         */
        player.setItemInHand(
                hand,
                ItemStack.EMPTY
        );

        player.drop(
                dropped,
                false
        );
    }
}