package net.itskiwibread.civtfg_progression.progression.mixin;

import net.itskiwibread.civtfg_progression.progression.InventoryProtection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class PlayerInventoryMixin {

    @Inject(
            method = "setItem",
            at = @At("HEAD")
    )
    private void civtfg$inventoryChanged(
            int slot,
            ItemStack stack,
            CallbackInfo ci
    ) {

        Inventory inventory =
                (Inventory) (Object) this;

        if (!(inventory.player instanceof ServerPlayer player)) {
            return;
        }

        ItemStack oldStack =
                inventory.getItem(slot);

        /*
         * Don't mark dirty if nothing actually changed.
         */
        if (ItemStack.matches(oldStack, stack)) {
            return;
        }

        InventoryProtection.markDirty(player);
    }
}