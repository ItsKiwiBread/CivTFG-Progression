package net.itskiwibread.civtfg_progression.progression;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PickupProtection {

    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ItemEntity itemEntity = event.getItem();

        ItemStack stack = itemEntity.getItem();

        int requiredGoal =
                ProgressionRules.requiredGoal(stack);

        // Not one of our locked test items.
        if (requiredGoal == -1) {
            return;
        }

        ProgressionManager progression =
                ProgressionManager.get(player.getServer());

        // Player/team has unlocked it.
        if (progression.hasGoal(player, requiredGoal)) {
            return;
        }

        /*

Prevent the normal ground pickup.*
Forge provides this event specifically for a player
colliding with an ItemEntity on the ground.*/
        event.setCanceled(true);

        player.sendSystemMessage(
                Component.literal(
                        "§cThis item is locked! §7Complete Goal "
                                + requiredGoal
                                + "."
                )
        );
    }
}