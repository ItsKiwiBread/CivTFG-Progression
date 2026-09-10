package net.itskiwibread.civtfg_progression.Menu;

import net.itskiwibread.civtfg_progression.CivTFG_Progression;
import net.itskiwibread.civtfg_progression.Menu.LaboratoryMenu;

import net.minecraft.world.inventory.MenuType;

import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, CivTFG_Progression.MOD_ID);

    public static final RegistryObject<MenuType<LaboratoryMenu>> LABORATORY_MENU =
            MENUS.register("laboratory_menu",
                    () -> IForgeMenuType.create(
                            (id, inventory,buffer) ->
                                    new LaboratoryMenu(id, inventory, buffer)
                    )
            );

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}