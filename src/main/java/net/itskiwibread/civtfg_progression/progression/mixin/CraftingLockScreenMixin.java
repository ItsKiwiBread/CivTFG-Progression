package net.itskiwibread.civtfg_progression.progression.mixin;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class CraftingLockScreenMixin {

    @Inject(
            method = "slotClicked",
            at = @At("HEAD"),
            cancellable = true
    )
    private void civtfg$blockLockedCraftingClient(
            Slot slot,
            int slotId,
            int button,
            ClickType clickType,
            CallbackInfo ci
    ) {

        if (!(slot instanceof ResultSlot)) {
            return;
        }

        ItemStack result = slot.getItem();

        if (result.isEmpty()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        /*
         * TEST MODE
         *
         * All three pickaxes are locked on the client.
         *
         * This is deliberately separate from ProgressionManager
         * for now. We first want to prove that the client-side
         * pickup is completely prevented.
         */

        if (result.is(Items.IRON_PICKAXE)) {

            minecraft.player.displayClientMessage(
                    Component.literal(
                            "§cIRON PICKAXE LOCKED!"
                    ),
                    true
            );

            ci.cancel();
            return;
        }

        if (result.is(Items.GOLDEN_PICKAXE)) {

            minecraft.player.displayClientMessage(
                    Component.literal(
                            "§6GOLD PICKAXE LOCKED!"
                    ),
                    true
            );

            ci.cancel();
            return;
        }

        if (result.is(Items.DIAMOND_PICKAXE)) {

            minecraft.player.displayClientMessage(
                    Component.literal(
                            "§bDIAMOND PICKAXE LOCKED!"
                    ),
                    true
            );

            ci.cancel();
        }
    }
}