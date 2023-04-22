package fr.tathan.bombastic.items;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.logging.log4j.core.jmx.Server;

public class CustomMinecartItem extends Item {

    static EntityType<?> minecartType;
    private static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

        @Override
        public ItemStack execute(BlockSource pSource, ItemStack pStack) {
            Direction direction = pSource.getBlockState().getValue(DispenserBlock.FACING);
            Level level = pSource.getLevel();
            double x = pSource.x() + (double)direction.getStepX() * 1.125D;
            double y = Math.floor(pSource.y()) + (double)direction.getStepY();
            double z = pSource.z() + (double)direction.getStepZ() * 1.125D;
            BlockPos blockPos = pSource.getPos().relative(direction);
            BlockState blockState = level.getBlockState(blockPos);
            RailShape $$9 = blockState.getBlock() instanceof BaseRailBlock ? blockState.getValue(((BaseRailBlock)blockState.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
            double $$10;
            if (blockState.is(BlockTags.RAILS)) {
                if ($$9.isAscending()) {
                    $$10 = 0.6D;
                } else {
                    $$10 = 0.1D;
                }
            } else {
                if (!blockState.isAir() || !level.getBlockState(blockPos.below()).is(BlockTags.RAILS)) {
                    return this.defaultDispenseItemBehavior.dispense(pSource, pStack);
                }

                BlockState $$12 = level.getBlockState(blockPos.below());
                RailShape $$13 = $$12.getBlock() instanceof BaseRailBlock ? $$12.getValue(((BaseRailBlock)$$12.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                if (direction != Direction.DOWN && $$13.isAscending()) {
                    $$10 = -0.4D;
                } else {
                    $$10 = -0.9D;
                }
            }

            if (pStack.hasCustomHoverName()) {
                minecartType.create(level).setCustomName(pStack.getHoverName());
            }
            minecartType.create(level);
            minecartType.spawn((ServerLevel)level, pStack, pSource.getLevel().getRandomPlayer(), blockPos.above(), MobSpawnType.MOB_SUMMONED, true, false);

            pStack.shrink(1);
            return pStack;
        }

        protected void playSound(BlockSource $$0) {
            $$0.getLevel().levelEvent(1000, $$0.getPos(), 0);
        }
    };

    public CustomMinecartItem(EntityType<?> minecartType, Item.Properties pProperties) {
        super(pProperties);
        this.minecartType = minecartType;
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos $$2 = pContext.getClickedPos();
        BlockState $$3 = level.getBlockState($$2);
        if (!$$3.is(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack $$4 = pContext.getItemInHand();
            if (!level.isClientSide) {
                RailShape $$5 = $$3.getBlock() instanceof BaseRailBlock ? $$3.getValue(((BaseRailBlock)$$3.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
                double $$6 = 0.0D;
                if ($$5.isAscending()) {
                    $$6 = 0.5D;
                }

                if ($$4.hasCustomHoverName()) {
                    minecartType.create(level).setCustomName($$4.getHoverName());
                }

                minecartType.spawn((ServerLevel)level, $$4, pContext.getPlayer(), $$2.above(), MobSpawnType.MOB_SUMMONED, true, false);
                level.gameEvent(GameEvent.ENTITY_PLACE, $$2, GameEvent.Context.of(pContext.getPlayer(), level.getBlockState($$2.below())));
            }

            $$4.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

}
