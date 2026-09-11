package net.itskiwibread.civtfg_progression.block;

import net.itskiwibread.civtfg_progression.block.entity.LaboratoryBlockEntity;
import net.itskiwibread.civtfg_progression.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class LaboratoryBlock extends BaseEntityBlock {

    public LaboratoryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit
    ) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);

            if (blockEntity instanceof LaboratoryBlockEntity) {
                NetworkHooks.openScreen(
                        (ServerPlayer) player,
                        (LaboratoryBlockEntity) blockEntity,
                        pos
                );
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type) {

        if (level.isClientSide) {
            return null;
        }

        return createTickerHelper(
                type,
                ModBlockEntities.LABORATORY_BLOCK_ENTITY.get(),
                (level1, pos, state1, blockEntity) -> blockEntity.tick()
        );
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LaboratoryBlockEntity(pos, state);
    }
}
