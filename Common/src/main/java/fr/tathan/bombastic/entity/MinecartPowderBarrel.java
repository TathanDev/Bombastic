package fr.tathan.bombastic.entity;

import fr.tathan.bombastic.registries.BlocksRegistry;
import fr.tathan.bombastic.registries.EntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class MinecartPowderBarrel extends MinecartTNT {
    private static final byte EVENT_PRIME = 10;
    private int fuse = -1;

    public MinecartPowderBarrel(EntityType<? extends MinecartPowderBarrel> $$0, Level $$1) {
        super($$0, $$1);
    }

    public MinecartPowderBarrel(Level $$0, double $$1, double $$2, double $$3) {
        super(EntitiesRegistry.TNT_MINECART.get(), $$0);
    }

    @Override
    protected Item getDropItem() {
        return Items.TNT_MINECART;
    }


    public AbstractMinecart.Type getMinecartType() {
        return AbstractMinecart.Type.TNT;
    }


    public BlockState getDefaultDisplayBlockState() {
        return BlocksRegistry.POWDER_BARREL.get().defaultBlockState();
    }


    public void tick() {
        super.tick();
        if (this.fuse > 0) {
            --this.fuse;
            this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
        } else if (this.fuse == 0) {
            this.explode(this.getDeltaMovement().horizontalDistanceSqr());
        }

        if (this.horizontalCollision) {
            double $$0 = this.getDeltaMovement().horizontalDistanceSqr();
            if ($$0 >= (double)0.01F) {
                this.explode($$0);
            }
        }

    }


    public boolean hurt(DamageSource $$0, float $$1) {
        Entity $$2 = $$0.getDirectEntity();
        if ($$2 instanceof AbstractArrow $$3) {
            if ($$3.isOnFire()) {
                this.explode($$3.getDeltaMovement().lengthSqr());
            }
        }

        return super.hurt($$0, $$1);
    }


    public void destroy(DamageSource $$0) {
        double $$1 = this.getDeltaMovement().horizontalDistanceSqr();
        if (!$$0.isFire() && !$$0.isExplosion() && !($$1 >= (double)0.01F)) {
            super.destroy($$0);
        } else {
            if (this.fuse < 0) {
                this.primeFuse();
                this.fuse = this.random.nextInt(20) + this.random.nextInt(20);
            }

        }
    }




    protected void explode(double $$0) {
        if (!this.level.isClientSide) {
            double $$1 = Math.sqrt($$0);
            if ($$1 > 5.0D) {
                $$1 = 5.0D;
            }

            this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)(4.0D + this.random.nextDouble() * 1.5D * $$1), Explosion.BlockInteraction.BREAK);
            this.discard();
        }

    }


    public boolean causeFallDamage(float $$0, float $$1, DamageSource $$2) {
        if ($$0 >= 3.0F) {
            float $$3 = $$0 / 10.0F;
            this.explode((double)($$3 * $$3));
        }

        return super.causeFallDamage($$0, $$1, $$2);
    }


    public void activateMinecart(int $$0, int $$1, int $$2, boolean $$3) {
        if ($$3 && this.fuse < 0) {
            this.primeFuse();
        }

    }


    public void handleEntityEvent(byte $$0) {
        if ($$0 == 10) {
            this.primeFuse();
        } else {
            super.handleEntityEvent($$0);
        }

    }


    public void primeFuse() {
        this.fuse = 80;
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)10);
            if (!this.isSilent()) {
                this.level.playSound((Player)null, this.getX(), this.getY(), this.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

    }

    public int getFuse() {
        return this.fuse;
    }


    public boolean isPrimed() {
        return this.fuse > -1;
    }

    public float getBlockExplosionResistance(Explosion $$0, BlockGetter $$1, BlockPos $$2, BlockState $$3, FluidState $$4, float $$5) {
        return !this.isPrimed() || !$$3.is(BlockTags.RAILS) && !$$1.getBlockState($$2.above()).is(BlockTags.RAILS) ? super.getBlockExplosionResistance($$0, $$1, $$2, $$3, $$4, $$5) : 0.0F;
    }

    public boolean shouldBlockExplode(Explosion $$0, BlockGetter $$1, BlockPos $$2, BlockState $$3, float $$4) {
        return !this.isPrimed() || !$$3.is(BlockTags.RAILS) && !$$1.getBlockState($$2.above()).is(BlockTags.RAILS) ? super.shouldBlockExplode($$0, $$1, $$2, $$3, $$4) : false;
    }

    protected void readAdditionalSaveData(CompoundTag $$0) {
        super.readAdditionalSaveData($$0);
        if ($$0.contains("TNTFuse", 99)) {
            this.fuse = $$0.getInt("TNTFuse");
        }

    }

    protected void addAdditionalSaveData(CompoundTag $$0) {
        super.addAdditionalSaveData($$0);
        $$0.putInt("TNTFuse", this.fuse);
    }



}
