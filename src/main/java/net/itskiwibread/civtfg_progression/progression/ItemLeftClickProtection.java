package net.itskiwibread.civtfg_progression.progression;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ItemLeftClickProtection {

    /*
     * Called when the player left-clicks a block.
     *
     * This prevents a locked pickaxe from being used to break blocks.
     */
    @SubscribeEvent
    public static void onLeftClickBlock(
            PlayerInteractEvent.LeftClickBlock event
    ) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack stack =
                player.getItemInHand(InteractionHand.MAIN_HAND);

        int requiredGoal =
                ProgressionRules.requiredGoal(stack);

        if (requiredGoal == -1) {
            return;
        }

        ProgressionManager progression =
                ProgressionManager.get(player.getServer());

        if (progression.hasGoal(
                player,
                requiredGoal
        )) {
            return;
        }

        event.setCanceled(true);

        ProgressionMessage.sendUseBlocked(
                player,
                stack,
                requiredGoal
        );
    }
}