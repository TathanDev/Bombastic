package fr.tathan.bombastic.entity;

import fr.tathan.bombastic.registries.EntitiesRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PrimedPowderBarrel extends Entity {
    private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(PrimedPowderBarrel.class, EntityDataSerializers.INT);
    private static final int DEFAULT_FUSE_TIME = 80;

    @Nullable
    private LivingEntity owner;

    public PrimedPowderBarrel(EntityType<? extends PrimedPowderBarrel> p_32076_, Level p_32077_) {
        super(p_32076_, p_32077_);
    }

    public PrimedPowderBarrel(Level pLevel, double x, double y, double z, @Nullable LivingEntity pOwner) {
        //super(pLevel, x, p_32081_, p_32082_, p_32083_);
        this(EntitiesRegistry.POWDER_BARREL.get(), pLevel);
        this.setPos(x, y, z);
        double d0 = pLevel.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.setDeltaMovement(-Math.sin(d0) * 0.02D, (double)0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(120);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.owner = pOwner;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_FUSE_ID, 80);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public void tick() {
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
        if (this.onGround) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
        }

        int i = this.getFuse() - 1;
        this.setFuse(i);
        if (i <= 0) {
            this.discard();
            if (!this.level.isClientSide) {
                this.explode();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level.isClientSide) {
                this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }


    protected void explode() {
        float f = 7.0F;
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), f, Explosion.BlockInteraction.BREAK);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_32097_) {
        p_32097_.putShort("Fuse", (short)this.getFuse());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_32091_) {
        this.setFuse(p_32091_.getShort("Fuse"));
    }


    public void setFuse(int p_32086_) {
        this.entityData.set(DATA_FUSE_ID, p_32086_);
    }


    public int getFuse() {
        return this.entityData.get(DATA_FUSE_ID);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }



}
