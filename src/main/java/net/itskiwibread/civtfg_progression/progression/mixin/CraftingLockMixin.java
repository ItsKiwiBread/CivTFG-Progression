package net.itskiwibread.civtfg_progression.progression.mixin;

import net.itskiwibread.civtfg_progression.progression.ProgressionManager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class CraftingLockMixin {

    @Inject(
            method = "clicked",
            at = @At("HEAD"),
            cancellable = true
    )
    private void civtfg$blockLockedCrafting(
            int slotId,
            int button,
            ClickType clickType,
            Player player,
            CallbackInfo ci
    ) {

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        AbstractContainerMenu menu =
                (AbstractContainerMenu) (Object) this;

        if (slotId < 0 || slotId >= menu.slots.size()) {
            return;
        }

        Slot slot = menu.getSlot(slotId);

        if (!(slot instanceof ResultSlot)) {
            return;
        }

        ItemStack result = slot.getItem();

        if (result.isEmpty()) {
            return;
        }

        ProgressionManager progression =
                ProgressionManager.get(serverPlayer.getServer());

        // Iron = Goal 1
        if (result.is(Items.IRON_PICKAXE)
                && !progression.hasGoal(serverPlayer, 1)) {

            serverPlayer.sendSystemMessage(
                    Component.literal(
                            "§cIRON PICKAXE LOCKED! §7Complete Goal 1."
                    )
            );

            ci.cancel();
            return;
        }

        // Gold = Goal 2
        if (result.is(Items.GOLDEN_PICKAXE)
                && !progression.hasGoal(serverPlayer, 2)) {

            serverPlayer.sendSystemMessage(
                    Component.literal(
                            "§6GOLD PICKAXE LOCKED! §7Complete Goal 2."
                    )
            );

            ci.cancel();
            return;
        }

        // Diamond = Goal 3
        if (result.is(Items.DIAMOND_PICKAXE)
                && !progression.hasGoal(serverPlayer, 3)) {

            serverPlayer.sendSystemMessage(
                    Component.literal(
                            "§bDIAMOND PICKAXE LOCKED! §7Complete Goal 3."
                    )
            );

            ci.cancel();
        }
    }
}