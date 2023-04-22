package fr.tathan.bombastic.blocks;

import fr.tathan.bombastic.entity.PrimedPowderBarrel;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class PowderBarrel extends Block {
    public static final BooleanProperty UNSTABLE;

    public PowderBarrel(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(UNSTABLE, false));
    }



    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && !player.isCreative() && (Boolean)state.getValue(UNSTABLE)) {
            explode(level, pos);
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void wasExploded(Level level, BlockPos pos, Explosion explosion) {
        if (!level.isClientSide) {
            PrimedPowderBarrel primedPowderBarrel = new PrimedPowderBarrel(level, (double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, explosion.getSourceMob());
            int i = primedPowderBarrel.getFuse();
            primedPowderBarrel.setFuse((short)(level.random.nextInt(i / 4) + i / 8));
            level.addFreshEntity(primedPowderBarrel);
        }
    }



    public static void explode(Level level, BlockPos pos) {
        explode(level, pos, (LivingEntity)null);
    }

    private static void explode(Level level, BlockPos pos, @Nullable LivingEntity entity) {
        if (!level.isClientSide) {
            PrimedPowderBarrel primedPowderBarrel = new PrimedPowderBarrel(level, (double)pos.getX() + 0.5, (double)pos.getY(), (double)pos.getZ() + 0.5, entity);
            level.addFreshEntity(primedPowderBarrel);
            level.playSound((Player)null, primedPowderBarrel.getX(), primedPowderBarrel.getY(), primedPowderBarrel.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.gameEvent(entity, GameEvent.PRIME_FUSE, pos);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!itemStack.is(Items.FLINT_AND_STEEL) && !itemStack.is(Items.FIRE_CHARGE)) {
            return super.use(state, level, pos, player, hand, hit);
        } else {
            explode(level, pos, player);
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
            Item item = itemStack.getItem();
            if (!player.isCreative()) {
                if (itemStack.is(Items.FLINT_AND_STEEL)) {
                    itemStack.hurtAndBreak(1, player, (playerx) -> {
                        playerx.broadcastBreakEvent(hand);
                    });
                } else {
                    itemStack.shrink(1);
                }
            }

            player.awardStat(Stats.ITEM_USED.get(item));
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!level.isClientSide) {
            BlockPos blockPos = hit.getBlockPos();
            Entity entity = projectile.getOwner();
            if (projectile.isOnFire() && projectile.mayInteract(level, blockPos)) {
                explode(level, blockPos, entity instanceof LivingEntity ? (LivingEntity)entity : null);
                level.removeBlock(blockPos, false);
            }
        }

    }

    @Override
    public boolean dropFromExplosion(Explosion explosion) {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{UNSTABLE});
    }

    static {
        UNSTABLE = BlockStateProperties.UNSTABLE;
    }

}

