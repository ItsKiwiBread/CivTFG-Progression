package net.itskiwibread.civtfg_progression.item;

import net.itskiwibread.civtfg_progression.CivTFG_Progression;
import net.itskiwibread.civtfg_progression.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CivTFG_Progression.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CIVTFG_PROGRESSION_TAB = CREATIVE_MODE_TABS.register("civtfg_progression_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.LABORATORY_BLOCK.get()))
                    .title(Component.translatable("creativetab.civtfg_progression_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModBlocks.LABORATORY_BLOCK.get());

                        pOutput.accept(ModItems.SAPPHIRE.get());

                        pOutput.accept(ModItems.MINING_SCIENCE_BRONZE.get());
                        pOutput.accept(ModItems.MINING_SCIENCE_IRON.get());
                        pOutput.accept(ModItems.MINING_SCIENCE_STEEL.get());
                        pOutput.accept(ModItems.MINING_SCIENCE_STEAM.get());
                        pOutput.accept(ModItems.MINING_SCIENCE_LV.get());
                        pOutput.accept(ModItems.MINING_SCIENCE_MV.get());
                        pOutput.accept(ModItems.MINING_SCIENCE_HV.get());
                        pOutput.accept(ModItems.MINING_SCIENCE_EV.get());
                        pOutput.accept(ModItems.MINING_SCIENCE_IV.get());

                        pOutput.accept(ModItems.FARMING_SCIENCE_BRONZE.get());
                        pOutput.accept(ModItems.FARMING_SCIENCE_IRON.get());
                        pOutput.accept(ModItems.FARMING_SCIENCE_STEEL.get());
                        pOutput.accept(ModItems.FARMING_SCIENCE_STEAM.get());
                        pOutput.accept(ModItems.FARMING_SCIENCE_LV.get());
                        pOutput.accept(ModItems.FARMING_SCIENCE_MV.get());
                        pOutput.accept(ModItems.FARMING_SCIENCE_HV.get());
                        pOutput.accept(ModItems.FARMING_SCIENCE_EV.get());
                        pOutput.accept(ModItems.FARMING_SCIENCE_IV.get());

                        pOutput.accept(ModItems.PRODUCTION_SCIENCE_BRONZE.get());
                        pOutput.accept(ModItems.PRODUCTION_SCIENCE_IRON.get());
                        pOutput.accept(ModItems.PRODUCTION_SCIENCE_STEEL.get());
                        pOutput.accept(ModItems.PRODUCTION_SCIENCE_STEAM.get());
                        pOutput.accept(ModItems.PRODUCTION_SCIENCE_LV.get());
                        pOutput.accept(ModItems.PRODUCTION_SCIENCE_MV.get());
                        pOutput.accept(ModItems.PRODUCTION_SCIENCE_HV.get());
                        pOutput.accept(ModItems.PRODUCTION_SCIENCE_EV.get());
                        pOutput.accept(ModItems.PRODUCTION_SCIENCE_IV.get());

                        pOutput.accept(ModItems.EXPLORATION_SCIENCE_BRONZE.get());
                        pOutput.accept(ModItems.EXPLORATION_SCIENCE_IRON.get());
                        pOutput.accept(ModItems.EXPLORATION_SCIENCE_STEEL.get());
                        pOutput.accept(ModItems.EXPLORATION_SCIENCE_STEAM.get());
                        pOutput.accept(ModItems.EXPLORATION_SCIENCE_LV.get());
                        pOutput.accept(ModItems.EXPLORATION_SCIENCE_MV.get());
                        pOutput.accept(ModItems.EXPLORATION_SCIENCE_HV.get());
                        pOutput.accept(ModItems.EXPLORATION_SCIENCE_EV.get());
                        pOutput.accept(ModItems.EXPLORATION_SCIENCE_IV.get());

                        pOutput.accept(ModItems.CHALLENGE_SCIENCE_BRONZE.get());
                        pOutput.accept(ModItems.CHALLENGE_SCIENCE_IRON.get());
                        pOutput.accept(ModItems.CHALLENGE_SCIENCE_STEEL.get());
                        pOutput.accept(ModItems.CHALLENGE_SCIENCE_STEAM.get());
                        pOutput.accept(ModItems.CHALLENGE_SCIENCE_LV.get());
                        pOutput.accept(ModItems.CHALLENGE_SCIENCE_MV.get());
                        pOutput.accept(ModItems.CHALLENGE_SCIENCE_HV.get());
                        pOutput.accept(ModItems.CHALLENGE_SCIENCE_EV.get());
                        pOutput.accept(ModItems.CHALLENGE_SCIENCE_IV.get());
                    })
                    .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
