package net.itskiwibread.civtfg_progression.block.entity;

import net.itskiwibread.civtfg_progression.CivTFG_Progression;
import net.itskiwibread.civtfg_progression.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CivTFG_Progression.MOD_ID);

    public static final RegistryObject<BlockEntityType<LaboratoryBlockEntity>> LABORATORY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("laboratory_block_entity",
                    () -> BlockEntityType.Builder.of(
                            LaboratoryBlockEntity::new,
                            ModBlocks.LABORATORY_BLOCK.get()
                    ).build(null));
}