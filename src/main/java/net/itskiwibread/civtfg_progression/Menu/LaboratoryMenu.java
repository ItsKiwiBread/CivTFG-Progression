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

public class LaboratoryMenu extends AbstractContainerMenu {

    // =========================================================
    // BLOCK ENTITY
    // =========================================================

    private final LaboratoryBlockEntity blockEntity;

    // =========================================================
    // DATA
    // =========================================================

    private final ContainerData data;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LaboratoryMenu(
            int id,
            Inventory inventory,
            LaboratoryBlockEntity blockEntity) {

        super(
                ModMenuTypes.LABORATORY_MENU.get(),
                id
        );

        this.blockEntity = blockEntity;

        // -----------------------------------------------------
        // Progress data
        //
        // 0 = consumption progress
        // 1 = laboratory progress
        // -----------------------------------------------------

        this.data = new ContainerData() {

            private final int[] values = new int[2];

            @Override
            public int get(int index) {

                if (blockEntity.getLevel() != null
                        && !blockEntity.getLevel().isClientSide) {

                    return switch (index) {

                        case 0 ->
                                blockEntity.getConsumeProgress();

                        case 1 ->
                                blockEntity.getLaboratoryProgress();

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
                return 2;
            }
        };

        // IMPORTANT:
        // Only add the data slots ONCE.
        addDataSlots(data);

        // =====================================================
        // LABORATORY INPUT SLOTS
        // =====================================================

        addSlot(
                new LaboratorySlot(
                        blockEntity,
                        0,
                        44,
                        31
                )
        );

        addSlot(
                new LaboratorySlot(
                        blockEntity,
                        1,
                        62,
                        31
                )
        );

        addSlot(
                new LaboratorySlot(
                        blockEntity,
                        2,
                        80,
                        31
                )
        );

        addSlot(
                new LaboratorySlot(
                        blockEntity,
                        3,
                        98,
                        31
                )
        );

        addSlot(
                new LaboratorySlot(
                        blockEntity,
                        4,
                        116,
                        31
                )
        );

        // =====================================================
        // PLAYER INVENTORY
        // =====================================================

        // Main inventory
        for (int row = 0; row < 3; row++) {

            for (int col = 0; col < 9; col++) {

                addSlot(
                        new Slot(
                                inventory,
                                col + row * 9 + 9,
                                7 + col * 18,
                                84 + row * 18
                        )
                );
            }
        }

        // Hotbar
        for (int col = 0; col < 9; col++) {

            addSlot(
                    new Slot(
                            inventory,
                            col,
                            7 + col * 18,
                            142
                    )
            );
        }
    }

    // =========================================================
    // CLIENT / NETWORK CONSTRUCTOR
    // =========================================================

    public static LaboratoryMenu fromNetwork(
            int id,
            Inventory inventory,
            FriendlyByteBuf buffer) {

        return null;
    }

    // =========================================================
    // PROGRESS
    // =========================================================

    public int getConsumeProgress() {
        return data.get(0);
    }

    public int getConsumeProgressMax() {
        return blockEntity.getConsumeProgressMax();
    }

    public int getLaboratoryProgress() {
        return data.get(1);
    }

    public int getLaboratoryProgressMax() {
        return blockEntity.getLaboratoryProgressMax();
    }

    // =========================================================
    // SHIFT CLICK
    // =========================================================

    @Override
    public ItemStack quickMoveStack(
            Player player,
            int index) {

        Slot slot = slots.get(index);

        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();

        ItemStack copy = stack.copy();

        // Laboratory slots are 0-4
        if (index < 5) {

            // Move laboratory item back to player inventory
            if (!moveItemStackTo(
                    stack,
                    5,
                    slots.size(),
                    true)) {

                return ItemStack.EMPTY;
            }

        } else {

            // Try to move player item into laboratory
            if (!moveItemStackTo(
                    stack,
                    0,
                    5,
                    false)) {

                return ItemStack.EMPTY;
            }
        }

        if (stack.isEmpty()) {

            slot.set(
                    ItemStack.EMPTY
            );

        } else {

            slot.setChanged();
        }

        return copy;
    }

    // =========================================================
    // VALIDITY
    // =========================================================

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }
}