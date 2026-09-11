package net.itskiwibread.civtfg_progression.progression;

import net.minecraft.world.item.ItemStack;

public final class ProgressionRules {

    private ProgressionRules() {
    }


    // ============================================================
    // REQUIRED GOAL
    // ============================================================

    public static int requiredGoal(
            ItemStack stack
    ) {

        return ProgressionItemManager.requiredGoal(
                stack
        );
    }


    // ============================================================
    // USE
    // ============================================================

    public static boolean shouldBlockUse(
            ItemStack stack
    ) {

        return ProgressionItemManager.shouldBlockUse(
                stack
        );
    }


    public static boolean shouldDropOnBlockedUse(
            ItemStack stack
    ) {

        return ProgressionItemManager
                .shouldDropOnBlockedUse(stack);
    }


    // ============================================================
    // MESSAGES
    // ============================================================

    public static String lockedPickupMessage(
            int goal
    ) {

        return "§cYou cannot have this item yet! §7Complete Goal "
                + goal
                + ".";
    }


    public static String lockedUseMessage(
            int goal
    ) {

        return "§cYou cannot use this item yet! §7Complete Goal "
                + goal
                + ".";
    }
}