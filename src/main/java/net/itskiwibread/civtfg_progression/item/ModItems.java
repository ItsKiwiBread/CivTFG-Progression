package net.itskiwibread.civtfg_progression.item;

import net.itskiwibread.civtfg_progression.CivTFG_Progression;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CivTFG_Progression.MOD_ID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    // =========================================================
// SCIENCE TYPES
// =========================================================

    public enum ScienceType {
        MINING,
        FARMING,
        PRODUCTION,
        EXPLORATION,
        CHALLENGE
    }

// =========================================================
// SCIENCE TIERS
// =========================================================

    public enum ScienceTier {
        BRONZE,
        IRON,
        STEEL,
        STEAM,
        LV,
        MV,
        HV,
        EV,
        IV
    }

// =========================================================
// SCIENCE ITEM HELPER
// =========================================================

    private static RegistryObject<Item> registerScienceItem(
            ScienceType type,
            ScienceTier tier) {

        String name =
                type.name().toLowerCase()
                        + "_science_"
                        + tier.name().toLowerCase();

        return ITEMS.register(
                name,
                () -> new Item(new Item.Properties())
        );
    }

    // =========================================================
// MINING SCIENCE
// =========================================================

    public static final RegistryObject<Item> MINING_SCIENCE_BRONZE =
            registerScienceItem(ScienceType.MINING, ScienceTier.BRONZE);

    public static final RegistryObject<Item> MINING_SCIENCE_IRON =
            registerScienceItem(ScienceType.MINING, ScienceTier.IRON);

    public static final RegistryObject<Item> MINING_SCIENCE_STEEL =
            registerScienceItem(ScienceType.MINING, ScienceTier.STEEL);

    public static final RegistryObject<Item> MINING_SCIENCE_STEAM =
            registerScienceItem(ScienceType.MINING, ScienceTier.STEAM);

    public static final RegistryObject<Item> MINING_SCIENCE_LV =
            registerScienceItem(ScienceType.MINING, ScienceTier.LV);

    public static final RegistryObject<Item> MINING_SCIENCE_MV =
            registerScienceItem(ScienceType.MINING, ScienceTier.MV);

    public static final RegistryObject<Item> MINING_SCIENCE_HV =
            registerScienceItem(ScienceType.MINING, ScienceTier.HV);

    public static final RegistryObject<Item> MINING_SCIENCE_EV =
            registerScienceItem(ScienceType.MINING, ScienceTier.EV);

    public static final RegistryObject<Item> MINING_SCIENCE_IV =
            registerScienceItem(ScienceType.MINING, ScienceTier.IV);


// =========================================================
// FARMING SCIENCE
// =========================================================

    public static final RegistryObject<Item> FARMING_SCIENCE_BRONZE =
            registerScienceItem(ScienceType.FARMING, ScienceTier.BRONZE);

    public static final RegistryObject<Item> FARMING_SCIENCE_IRON =
            registerScienceItem(ScienceType.FARMING, ScienceTier.IRON);

    public static final RegistryObject<Item> FARMING_SCIENCE_STEEL =
            registerScienceItem(ScienceType.FARMING, ScienceTier.STEEL);

    public static final RegistryObject<Item> FARMING_SCIENCE_STEAM =
            registerScienceItem(ScienceType.FARMING, ScienceTier.STEAM);

    public static final RegistryObject<Item> FARMING_SCIENCE_LV =
            registerScienceItem(ScienceType.FARMING, ScienceTier.LV);

    public static final RegistryObject<Item> FARMING_SCIENCE_MV =
            registerScienceItem(ScienceType.FARMING, ScienceTier.MV);

    public static final RegistryObject<Item> FARMING_SCIENCE_HV =
            registerScienceItem(ScienceType.FARMING, ScienceTier.HV);

    public static final RegistryObject<Item> FARMING_SCIENCE_EV =
            registerScienceItem(ScienceType.FARMING, ScienceTier.EV);

    public static final RegistryObject<Item> FARMING_SCIENCE_IV =
            registerScienceItem(ScienceType.FARMING, ScienceTier.IV);


// =========================================================
// PRODUCTION SCIENCE
// =========================================================

    public static final RegistryObject<Item> PRODUCTION_SCIENCE_BRONZE =
            registerScienceItem(ScienceType.PRODUCTION, ScienceTier.BRONZE);

    public static final RegistryObject<Item> PRODUCTION_SCIENCE_IRON =
            registerScienceItem(ScienceType.PRODUCTION, ScienceTier.IRON);

    public static final RegistryObject<Item> PRODUCTION_SCIENCE_STEEL =
            registerScienceItem(ScienceType.PRODUCTION, ScienceTier.STEEL);

    public static final RegistryObject<Item> PRODUCTION_SCIENCE_STEAM =
            registerScienceItem(ScienceType.PRODUCTION, ScienceTier.STEAM);

    public static final RegistryObject<Item> PRODUCTION_SCIENCE_LV =
            registerScienceItem(ScienceType.PRODUCTION, ScienceTier.LV);

    public static final RegistryObject<Item> PRODUCTION_SCIENCE_MV =
            registerScienceItem(ScienceType.PRODUCTION, ScienceTier.MV);

    public static final RegistryObject<Item> PRODUCTION_SCIENCE_HV =
            registerScienceItem(ScienceType.PRODUCTION, ScienceTier.HV);

    public static final RegistryObject<Item> PRODUCTION_SCIENCE_EV =
            registerScienceItem(ScienceType.PRODUCTION, ScienceTier.EV);

    public static final RegistryObject<Item> PRODUCTION_SCIENCE_IV =
            registerScienceItem(ScienceType.PRODUCTION, ScienceTier.IV);


// =========================================================
// EXPLORATION SCIENCE
// =========================================================

    public static final RegistryObject<Item> EXPLORATION_SCIENCE_BRONZE =
            registerScienceItem(ScienceType.EXPLORATION, ScienceTier.BRONZE);

    public static final RegistryObject<Item> EXPLORATION_SCIENCE_IRON =
            registerScienceItem(ScienceType.EXPLORATION, ScienceTier.IRON);

    public static final RegistryObject<Item> EXPLORATION_SCIENCE_STEEL =
            registerScienceItem(ScienceType.EXPLORATION, ScienceTier.STEEL);

    public static final RegistryObject<Item> EXPLORATION_SCIENCE_STEAM =
            registerScienceItem(ScienceType.EXPLORATION, ScienceTier.STEAM);

    public static final RegistryObject<Item> EXPLORATION_SCIENCE_LV =
            registerScienceItem(ScienceType.EXPLORATION, ScienceTier.LV);

    public static final RegistryObject<Item> EXPLORATION_SCIENCE_MV =
            registerScienceItem(ScienceType.EXPLORATION, ScienceTier.MV);

    public static final RegistryObject<Item> EXPLORATION_SCIENCE_HV =
            registerScienceItem(ScienceType.EXPLORATION, ScienceTier.HV);

    public static final RegistryObject<Item> EXPLORATION_SCIENCE_EV =
            registerScienceItem(ScienceType.EXPLORATION, ScienceTier.EV);

    public static final RegistryObject<Item> EXPLORATION_SCIENCE_IV =
            registerScienceItem(ScienceType.EXPLORATION, ScienceTier.IV);


// =========================================================
// CHALLENGE SCIENCE
// =========================================================

    public static final RegistryObject<Item> CHALLENGE_SCIENCE_BRONZE =
            registerScienceItem(ScienceType.CHALLENGE, ScienceTier.BRONZE);

    public static final RegistryObject<Item> CHALLENGE_SCIENCE_IRON =
            registerScienceItem(ScienceType.CHALLENGE, ScienceTier.IRON);

    public static final RegistryObject<Item> CHALLENGE_SCIENCE_STEEL =
            registerScienceItem(ScienceType.CHALLENGE, ScienceTier.STEEL);

    public static final RegistryObject<Item> CHALLENGE_SCIENCE_STEAM =
            registerScienceItem(ScienceType.CHALLENGE, ScienceTier.STEAM);

    public static final RegistryObject<Item> CHALLENGE_SCIENCE_LV =
            registerScienceItem(ScienceType.CHALLENGE, ScienceTier.LV);

    public static final RegistryObject<Item> CHALLENGE_SCIENCE_MV =
            registerScienceItem(ScienceType.CHALLENGE, ScienceTier.MV);

    public static final RegistryObject<Item> CHALLENGE_SCIENCE_HV =
            registerScienceItem(ScienceType.CHALLENGE, ScienceTier.HV);

    public static final RegistryObject<Item> CHALLENGE_SCIENCE_EV =
            registerScienceItem(ScienceType.CHALLENGE, ScienceTier.EV);

    public static final RegistryObject<Item> CHALLENGE_SCIENCE_IV =
            registerScienceItem(ScienceType.CHALLENGE, ScienceTier.IV);
}
