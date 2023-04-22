package fr.tathan.bombastic.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import fr.tathan.bombastic.entity.MinecartPowderBarrel;
import fr.tathan.bombastic.registries.BlocksRegistry;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class MinecartPowderBarrelRenderer extends MinecartRenderer<MinecartPowderBarrel> {
    private final BlockRenderDispatcher blockRenderer;

    public MinecartPowderBarrelRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, ModelLayers.TNT_MINECART);
        this.blockRenderer = pContext.getBlockRenderDispatcher();
    }

    protected void renderMinecartContents(MinecartPowderBarrel $$0, float $$1, BlockState $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        int $$6 = $$0.getFuse();
        if ($$6 > -1 && (float)$$6 - $$1 + 1.0F < 10.0F) {
            float $$7 = 1.0F - ((float)$$6 - $$1 + 1.0F) / 10.0F;
            $$7 = Mth.clamp($$7, 0.0F, 1.0F);
            $$7 *= $$7;
            $$7 *= $$7;
            float $$8 = 1.0F + $$7 * 0.3F;
            $$3.scale($$8, $$8, $$8);
        }

        renderWhiteSolidBlock(this.blockRenderer, $$2, $$3, $$4, $$5, $$6 > -1 && $$6 / 5 % 2 == 0);
    }

    public static void renderWhiteSolidBlock(BlockRenderDispatcher $$0, BlockState $$1, PoseStack $$2, MultiBufferSource $$3, int $$4, boolean $$5) {
        int $$6;
        if ($$5) {
            $$6 = OverlayTexture.pack(OverlayTexture.u(1.0F), 10);
        } else {
            $$6 = OverlayTexture.NO_OVERLAY;
        }

        $$0.renderSingleBlock($$1, $$2, $$3, $$4, $$6);
    }


}
