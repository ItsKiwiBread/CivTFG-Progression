package net.itskiwibread.civtfg_progression.block.entity;

import net.itskiwibread.civtfg_progression.Menu.LaboratoryMenu;
import net.itskiwibread.civtfg_progression.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LaboratoryBlockEntity extends BlockEntity
        implements net.minecraft.world.MenuProvider, Container {

    // =========================================================
    // INVENTORY
    // =========================================================

    // Five laboratory input slots
    private final ItemStack[] items = new ItemStack[5];

    // =========================================================
    // PROGRESS
    // =========================================================

    // Shared consumption progress
    private int consumeProgress = 0;

    // Overall laboratory progress
    private int laboratoryProgress = 0;

    // How many ticks it takes to consume one batch
    private static final int CONSUME_TIME = 100;

    // Maximum laboratory progress
    private static final int MAX_PROGRESS = 100;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LaboratoryBlockEntity(BlockPos pos, BlockState state) {
        super(net.itskiwibread.civtfg_progression.block.entity.ModBlockEntities.LABORATORY_BLOCK_ENTITY.get(), pos, state);

        for (int i = 0; i < items.length; i++) {
            items[i] = ItemStack.EMPTY;
        }
    }

    // =========================================================
    // TICK
    // =========================================================

    public static void tick(
            Level level,
            BlockPos pos,
            BlockState state,
            LaboratoryBlockEntity entity) {

        if (level.isClientSide) {
            return;
        }

        // -----------------------------------------------------
        // Check whether there is anything to consume
        // -----------------------------------------------------

        boolean hasItems = false;

        for (int slot = 0; slot < 5; slot++) {

            if (!entity.items[slot].isEmpty()
                    && entity.isItemValid(slot, entity.items[slot])) {

                hasItems = true;
                break;
            }
        }

        // Nothing to consume
        if (!hasItems) {
            entity.consumeProgress = 0;
            entity.setChanged();
            return;
        }

        // -----------------------------------------------------
        // Increase shared consumption progress
        // -----------------------------------------------------

        entity.consumeProgress++;

        // -----------------------------------------------------
        // Consumption cycle completed
        // -----------------------------------------------------

        if (entity.consumeProgress >= CONSUME_TIME) {

            int itemsConsumed = 0;

            // Consume ONE item from EVERY occupied valid slot
            for (int slot = 0; slot < 5; slot++) {

                if (!entity.items[slot].isEmpty()
                        && entity.isItemValid(slot, entity.items[slot])) {

                    entity.items[slot].shrink(1);

                    itemsConsumed++;
                }
            }

            // -------------------------------------------------
            // Add laboratory progress
            // -------------------------------------------------

            entity.laboratoryProgress += itemsConsumed;

            // Prevent progress from exceeding maximum
            if (entity.laboratoryProgress > MAX_PROGRESS) {
                entity.laboratoryProgress = MAX_PROGRESS;
            }

            // Reset shared consumption timer
            entity.consumeProgress = 0;
        }

        entity.setChanged();
    }

    // =========================================================
    // SLOT RESTRICTIONS
    // =========================================================

    public boolean isItemValid(int slot, ItemStack stack) {

        if (stack.isEmpty()) {
            return false;
        }

        return switch (slot) {

            // Slot 0 = pickaxe
            case 0 -> stack.getItem() instanceof net.minecraft.world.item.PickaxeItem;

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
        return Component.translatable(
                "container.civtfg_progression.laboratory"
        );
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
    // CONTAINER
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

        return Container.stillValidBlockEntity(
                this,
                player
        );
    }

    @Override
    public void clearContent() {

        for (int i = 0; i < items.length; i++) {
            items[i] = ItemStack.EMPTY;
        }

        consumeProgress = 0;

        setChanged();
    }

    // =========================================================
    // SAVE
    // =========================================================

    @Override
    protected void saveAdditional(CompoundTag tag) {

        super.saveAdditional(tag);

        // -----------------------------------------------------
        // Save inventory
        // -----------------------------------------------------

        ListTag itemsTag = new ListTag();

        for (int i = 0; i < items.length; i++) {

            if (!items[i].isEmpty()) {

                CompoundTag itemTag = new CompoundTag();

                itemTag.putByte(
                        "Slot",
                        (byte) i
                );

                items[i].save(itemTag);

                itemsTag.add(itemTag);
            }
        }

        tag.put("Items", itemsTag);

        // -----------------------------------------------------
        // Save progress
        // -----------------------------------------------------

        tag.putInt(
                "ConsumeProgress",
                consumeProgress
        );

        tag.putInt(
                "LaboratoryProgress",
                laboratoryProgress
        );
    }

    // =========================================================
    // LOAD
    // =========================================================

    @Override
    public void load(CompoundTag tag) {

        super.load(tag);

        // -----------------------------------------------------
        // Clear inventory
        // -----------------------------------------------------

        for (int i = 0; i < items.length; i++) {
            items[i] = ItemStack.EMPTY;
        }

        // -----------------------------------------------------
        // Load inventory
        // -----------------------------------------------------

        ListTag itemsTag = tag.getList(
                "Items",
                Tag.TAG_COMPOUND
        );

        for (int i = 0; i < itemsTag.size(); i++) {

            CompoundTag itemTag =
                    itemsTag.getCompound(i);

            int slot =
                    itemTag.getByte("Slot") & 255;

            if (slot >= 0 && slot < items.length) {

                items[slot] =
                        ItemStack.of(itemTag);
            }
        }

        // -----------------------------------------------------
        // Load progress
        // -----------------------------------------------------

        consumeProgress =
                tag.getInt("ConsumeProgress");

        laboratoryProgress =
                tag.getInt("LaboratoryProgress");

        // Safety
        if (consumeProgress < 0) {
            consumeProgress = 0;
        }

        if (consumeProgress > CONSUME_TIME) {
            consumeProgress = CONSUME_TIME;
        }

        if (laboratoryProgress < 0) {
            laboratoryProgress = 0;
        }

        if (laboratoryProgress > MAX_PROGRESS) {
            laboratoryProgress = MAX_PROGRESS;
        }
    }

    // =========================================================
    // PROGRESS GETTERS
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