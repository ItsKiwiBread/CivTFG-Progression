package net.itskiwibread.civtfg_progression.client;

import net.itskiwibread.civtfg_progression.CivTFG_Progression;
import net.itskiwibread.civtfg_progression.Menu.ModMenuTypes;

import net.minecraft.client.gui.screens.MenuScreens;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = CivTFG_Progression.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(
                    ModMenuTypes.LABORATORY_MENU.get(),
                    LaboratoryScreen::new
            );
        });
    }
}