package net.itskiwibread.civtfg_progression.progression;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class ProgressionRules {

    private ProgressionRules() {
    }

    /*

============================================================
TEST SWITCH
============================================================*
true:
If a locked item somehow reaches the player's inventory,
immediately remove it and drop it on the ground.*
false:
Do not perform the emergency inventory removal.*
This makes testing the system much easier.*/
    public static boolean DROP_FORBIDDEN_ITEMS = true;


    /*

============================================================
TEST PROGRESSION
============================================================*
Iron   -> Goal 1
Gold   -> Goal 2
Diamond -> Goal 3*/

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

        // -1 = item isn't progression locked
        return -1;
    }
}