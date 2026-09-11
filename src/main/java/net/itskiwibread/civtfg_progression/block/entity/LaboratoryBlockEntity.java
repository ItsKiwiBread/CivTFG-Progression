package net.itskiwibread.civtfg_progression.block.entity;

import net.itskiwibread.civtfg_progression.Menu.ModMenuTypes;
import net.itskiwibread.civtfg_progression.Menu.LaboratoryMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LaboratoryBlockEntity extends BlockEntity
        implements MenuProvider, Container {

    // Five laboratory input slots
    private final ItemStack[] items = new ItemStack[5];

    // How far the CURRENT item has been consumed
    private int consumeProgress = 0;

    // Overall laboratory progress
    private int laboratoryProgress = 0;

    // How many ticks it takes to consume one item
    private static final int CONSUME_TIME = 100;

    // Maximum overall progress
    private static final int MAX_PROGRESS = 100;

    public LaboratoryBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LABORATORY_BLOCK_ENTITY.get(), pos, state);

        for (int i = 0; i < items.length; i++) {
            items[i] = ItemStack.EMPTY;
        }
    }

    // =========================================================
    // TICK
    // =========================================================

    public void tick() {

        // Find an item to consume
        int slot = getNextOccupiedSlot();

        if (slot == -1) {
            // Nothing to consume
            consumeProgress = 0;
            return;
        }

        ItemStack stack = items[slot];

        // Make sure the item is actually valid for this slot
        if (!isItemValid(slot, stack)) {
            consumeProgress = 0;
            return;
        }

        // Advance consumption
        consumeProgress++;

        // Finished consuming one item
        if (consumeProgress >= CONSUME_TIME) {

            consumeProgress = 0;

            // Remove one item
            stack.shrink(1);

            // Add progress to the second bar
            laboratoryProgress++;

            // Mark block entity as changed
            setChanged();

            // If the laboratory is complete
            if (laboratoryProgress >= MAX_PROGRESS) {
                laboratoryProgress = 0;

                // TODO:
                // Put your completed laboratory result here.
            }
        }

        setChanged();
    }

    // =========================================================
    // FIND NEXT ITEM
    // =========================================================

    private int getNextOccupiedSlot() {

        for (int i = 0; i < items.length; i++) {

            if (!items[i].isEmpty() && isItemValid(i, items[i])) {
                return i;
            }
        }

        return -1;
    }

    // =========================================================
    // SLOT RESTRICTIONS
    // =========================================================

    public boolean isItemValid(int slot, ItemStack stack) {

        if (stack.isEmpty()) {
            return false;
        }

        return switch (slot) {

            // Slot 0 = normal pickaxe
            case 0 -> stack.getItem() instanceof net.minecraft.world.item.PickaxeItem
                    && !(stack.getItem() instanceof net.minecraft.world.item.DiggerItem
                    && stack.getItem() instanceof net.minecraft.world.item.PickaxeItem);

            // Slot 1 = diamond pickaxe
            case 1 -> stack.is(net.minecraft.world.item.Items.DIAMOND_PICKAXE);

            // Slot 2 = iron ingot
            case 2 -> stack.is(net.minecraft.world.item.Items.IRON_INGOT);

            // Slot 3 = redstone
            case 3 -> stack.is(net.minecraft.world.item.Items.REDSTONE);

            // Slot 4 = glass bottle
            case 4 -> stack.is(net.minecraft.world.item.Items.GLASS_BOTTLE);

            default -> false;
        };
    }

    // =========================================================
    // MENU
    // =========================================================

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.civtfg_progression.laboratory");
    }

    @Override
    public AbstractContainerMenu createMenu(
            int id,
            Inventory inventory,
            Player player) {

        return new LaboratoryMenu(
                id,
                inventory,
                this
        );
    }

    // =========================================================
    // CONTAINER IMPLEMENTATION
    // =========================================================

    @Override
    public int getContainerSize() {
        return 5;
    }

    @Override
    public boolean isEmpty() {

        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items[slot];
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {

        ItemStack stack = items[slot];

        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack result = stack.split(amount);

        setChanged();

        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {

        ItemStack stack = items[slot];

        items[slot] = ItemStack.EMPTY;

        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {

        items[slot] = stack;

        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {

        if (level == null) {
            return false;
        }

        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {

        for (int i = 0; i < items.length; i++) {
            items[i] = ItemStack.EMPTY;
        }

        setChanged();
    }

    // =========================================================
    // PROGRESS
    // =========================================================

    public int getConsumeProgress() {
        return consumeProgress;
    }

    public int getConsumeProgressMax() {
        return CONSUME_TIME;
    }

    public int getLaboratoryProgress() {
        return laboratoryProgress;
    }

    public int getLaboratoryProgressMax() {
        return MAX_PROGRESS;
    }
}