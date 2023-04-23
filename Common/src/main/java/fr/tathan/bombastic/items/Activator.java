package fr.tathan.bombastic.items;

import com.mojang.datafixers.kinds.Const;
import fr.tathan.bombastic.Constants;
import fr.tathan.bombastic.blocks.Detonator;
import fr.tathan.bombastic.registries.BlocksRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.gameevent.GameEvent;

public class Activator extends Item {



    public Activator( Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (!blockState.is(BlocksRegistry.DETONATOR.get())) {
            return InteractionResult.FAIL;
        } else {

            ItemStack stack = pContext.getItemInHand();

            CompoundTag coords = stack.getOrCreateTagElement("coords");
            coords.putInt("x", blockPos.getX());
            coords.putInt("y", blockPos.getY());
            coords.putInt("z", blockPos.getZ());

            Constants.LOG.info("x: " + coords.getInt("x") + " y: " + coords.getInt("y") + " z: " + coords.getInt("z"));

            stack.setHoverName(Component.literal("x: " + coords.getInt("x") + " y: " + coords.getInt("y") + " z: " + coords.getInt("z")));

            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        CompoundTag coords = stack.getTagElement("coords");

        if (coords == null) {
            player.sendSystemMessage(Component.translatable("message.bombastic.no_coords"));
            return InteractionResultHolder.fail(stack);

        }

            BlockPos blockPos = new BlockPos(coords.getInt("x"), coords.getInt("y"), coords.getInt("z"));
            BlockState blockState = pLevel.getBlockState(blockPos);
            Constants.LOG.info(blockPos.toString());
            Constants.LOG.info("x: " + coords.getInt("x") + " y: " + coords.getInt("y") + " z: " + coords.getInt("z"));


            if (blockState.getBlock() instanceof Detonator detonator) {
                detonator.activate(blockState, pLevel, blockPos);
                Constants.LOG.info("Activated");
                pLevel.playSound((Player) null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.NEUTRAL, 1.0F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));

                return InteractionResultHolder.success(stack);

            }

        return InteractionResultHolder.pass(stack);

    }

}





