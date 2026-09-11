package net.itskiwibread.civtfg_progression.progression;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ProgressionRules {

    private ProgressionRules() {
    }

    /*
     * ============================================================
     * TEST / SAFETY SETTINGS
     * ============================================================
     */

    /*
     * If true:
     *
     * Any locked progression item that somehow reaches a player's
     * inventory is immediately removed and dropped.
     *
     * Set this to false when you want to test what happens without
     * the emergency eject system.
     */
    public static boolean DROP_FORBIDDEN_ITEMS = true;


    /*
     * ============================================================
     * TEST ITEM -> REQUIRED GOAL
     * ============================================================
     *
     * -1 means "this item isn't progression locked".
     */

    public static int requiredGoal(ItemStack stack) {

        if (stack.is(Items.IRON_PICKAXE)) {
            return 1;
        }

        if (stack.is(Items.GOLDEN_PICKAXE)) {
            return 2;
        }

        if (stack.is(Items.DIAMOND_PICKAXE)) {
            return 3;
        }

        return -1;
    }


    /*
     * ============================================================
     * MESSAGE
     * ============================================================
     */

    public static String lockedPickupMessage(int goal) {

        return "§cYou cannot have this item yet! §7Complete Goal "
                + goal
                + ".";
    }

    public static String lockedUseMessage(int goal) {

        return "§cYou cannot use this item yet! §7Complete Goal "
                + goal
                + ".";
    }
}