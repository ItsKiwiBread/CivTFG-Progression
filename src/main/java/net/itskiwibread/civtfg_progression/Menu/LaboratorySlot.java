package net.itskiwibread.civtfg_progression.Menu;

import net.itskiwibread.civtfg_progression.block.entity.LaboratoryBlockEntity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class LaboratorySlot extends Slot {

    private final LaboratoryBlockEntity laboratory;

    public LaboratorySlot(
            LaboratoryBlockEntity laboratory,
            int slot,
            int x,
            int y) {

        super(laboratory, slot, x, y);

        this.laboratory = laboratory;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {

        return laboratory.isItemValid(getContainerSlot(), stack);
    }

    @Override
    public int getMaxStackSize() {

        return 64;
    }
}