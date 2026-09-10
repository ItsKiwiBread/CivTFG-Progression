package net.itskiwibread.civtfg_progression.progression;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ProgressionCraftingTest {

    @SubscribeEvent
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {

        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ProgressionManager manager =
                ProgressionManager.get(player.getServer());

        ItemStack crafted =
                event.getCrafting();


        // ============================================================
        // IRON PICKAXE
        // ============================================================

        if (crafted.is(Items.IRON_PICKAXE)) {

            if (!manager.hasGoal(player, 1)) {

                player.sendSystemMessage(
                        net.minecraft.network.chat.Component.literal(
                                "§cIRON PICKAXE: LOCKED - Complete Goal 1."
                        )
                );
            }
        }


        // ============================================================
        // GOLD PICKAXE
        // ============================================================

        if (crafted.is(Items.GOLDEN_PICKAXE)) {

            if (!manager.hasGoal(player, 2)) {

                player.sendSystemMessage(
                        net.minecraft.network.chat.Component.literal(
                                "§cGOLD PICKAXE: LOCKED - Complete Goal 2."
                        )
                );
            }
        }


        // ============================================================
        // DIAMOND PICKAXE
        // ============================================================

        if (crafted.is(Items.DIAMOND_PICKAXE)) {

            if (!manager.hasGoal(player, 3)) {

                player.sendSystemMessage(
                        net.minecraft.network.chat.Component.literal(
                                "§cDIAMOND PICKAXE: LOCKED - Complete Goal 3."
                        )
                );
            }
        }
    }
}