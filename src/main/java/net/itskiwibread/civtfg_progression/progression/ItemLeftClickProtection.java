package net.itskiwibread.civtfg_progression.progression;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ItemLeftClickProtection {

    @SubscribeEvent
    public static void onLeftClickBlock(
            PlayerInteractEvent.LeftClickBlock event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack stack =
                player.getItemInHand(
                        InteractionHand.MAIN_HAND
                );

        int goal =
                ProgressionRules.requiredGoal(stack);

        /*
         * Not a progression-controlled item.
         */
        if (goal == -1) {
            return;
        }

        ProgressionManager progression =
                ProgressionManager.get(
                        player.getServer()
                );

        /*
         * The player has unlocked the required tier.
         */
        if (progression.hasGoal(
                player,
                goal
        )) {
            return;
        }

        /*
         * Don't allow the locked tool to break blocks.
         */
        event.setCanceled(true);

        ProgressionMessage.sendUseBlocked(
                player,
                stack,
                goal
        );

        /*
         * Drop it if configured to do so.
         */
        if (ProgressionRules
                .shouldDropOnBlockedUse(stack)) {

            ItemStack dropped =
                    stack.copy();

            player.setItemInHand(
                    InteractionHand.MAIN_HAND,
                    ItemStack.EMPTY
            );

            player.drop(
                    dropped,
                    false
            );
        }
    }
}