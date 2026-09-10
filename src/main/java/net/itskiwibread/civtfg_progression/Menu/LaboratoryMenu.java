package net.itskiwibread.civtfg_progression.Menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class LaboratoryMenu extends AbstractContainerMenu {

    private final Container container;

    // Client constructor
    public LaboratoryMenu(int id, Inventory inventory, FriendlyByteBuf buffer) {
        this(id, inventory, new SimpleContainer(5));
    }

    // Server constructor
    public LaboratoryMenu(int id, Inventory inventory, Container container) {
        super(ModMenuTypes.LABORATORY_MENU.get(), id);

        this.container = container;

        // =====================================
        // Laboratory slots
        // =====================================

        // Slot 1
        this.addSlot(new Slot(container, 0, 44, 31));

        // Slot 2
        this.addSlot(new Slot(container, 1, 62, 31));

        // Slot 3
        this.addSlot(new Slot(container, 2, 80, 31));

        // Slot 4
        this.addSlot(new Slot(container, 3, 98, 31));

        // Slot 5
        this.addSlot(new Slot(container, 4, 116, 31));


        // =====================================
        // Player inventory
        // =====================================

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {

                this.addSlot(new Slot(
                        inventory,
                        col + row * 9 + 9,
                        8 + col * 18,
                        84 + row * 18
                ));
            }
        }


        // =====================================
        // Hotbar
        // =====================================

        for (int col = 0; col < 9; col++) {

            this.addSlot(new Slot(
                    inventory,
                    col,
                    8 + col * 18,
                    142
            ));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }
}