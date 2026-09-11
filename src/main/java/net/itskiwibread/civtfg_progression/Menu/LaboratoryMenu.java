package net.itskiwibread.civtfg_progression.Menu;

import net.itskiwibread.civtfg_progression.block.entity.LaboratoryBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;

public class LaboratoryMenu extends AbstractContainerMenu {

    private final LaboratoryBlockEntity blockEntity;

    private final ContainerData data;

    public LaboratoryMenu(int id, Inventory inventory, LaboratoryBlockEntity blockEntity) {
        super(ModMenuTypes.LABORATORY_MENU.get(), id);

        this.blockEntity = blockEntity;

        this.data = new ContainerData() {

            private final int[] values = new int[4];

            @Override
            public int get(int index) {
                if (blockEntity.getLevel() != null &&
                        !blockEntity.getLevel().isClientSide) {

                    return switch (index) {
                        case 0 -> blockEntity.getConsumeProgress();
                        case 1 -> blockEntity.getConsumeProgressMax();
                        case 2 -> blockEntity.getLaboratoryProgress();
                        case 3 -> blockEntity.getLaboratoryProgressMax();
                        default -> 0;
                    };
                }

                return values[index];
            }

            @Override
            public void set(int index, int value) {
                values[index] = value;
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
        // This is IMPORTANT
        addDataSlots(data);

        // =====================================================
        // LABORATORY INPUT SLOTS
        // =====================================================

        addSlot(new LaboratorySlot(blockEntity, 0, 44, 31));
        addSlot(new LaboratorySlot(blockEntity, 1, 62, 31));
        addSlot(new LaboratorySlot(blockEntity, 2, 80, 31));
        addSlot(new LaboratorySlot(blockEntity, 3, 98, 31));
        addSlot(new LaboratorySlot(blockEntity, 4, 116, 31));

        // =====================================================
        // PLAYER INVENTORY
        // =====================================================

        // Main inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(
                        inventory,
                        col + row * 9 + 9,
                        7 + col * 18,
                        84 + row * 18
                ));
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(
                    inventory,
                    col,
                    7 + col * 18,
                    142
            ));
        }

        addDataSlots(data);
    }

    // =========================================================
    // CLIENT CONSTRUCTOR
    // =========================================================

    public static LaboratoryMenu fromNetwork(
            int id,
            Inventory inventory,
            FriendlyByteBuf buffer) {

        // If you're using BlockEntityMenuProvider/network position,
        // you can read the BlockPos here.
        return null;
    }

    // =========================================================
    // PROGRESS
    // =========================================================

    public int getConsumeProgress() {
        return data.get(0);
    }

    public int getConsumeProgressMax() {
        return data.get(1);
    }

    public int getLaboratoryProgress() {
        return data.get(2);
    }

    public int getLaboratoryProgressMax() {
        return data.get(3);
    }

    // =========================================================
    // SHIFT CLICK
    // =========================================================

    @Override
    public ItemStack quickMoveStack(Player player, int index) {

        Slot slot = slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack copy = stack.copy();

        // Laboratory slots are 0-4
        if (index < 5) {

            if (!moveItemStackTo(
                    stack,
                    5,
                    slots.size(),
                    true)) {

                return ItemStack.EMPTY;
            }

        } else {

            // Try to put into laboratory slots
            if (!moveItemStackTo(
                    stack,
                    0,
                    5,
                    false)) {

                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return copy;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }
}