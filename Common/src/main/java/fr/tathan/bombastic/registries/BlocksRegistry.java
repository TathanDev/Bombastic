package fr.tathan.bombastic.registries;

import fr.tathan.bombastic.Constants;
import fr.tathan.bombastic.blocks.PowderBarrel;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;

public class BlocksRegistry {
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registry.BLOCK, Constants.MODID);

    public static RegistryObject<Block> POWDER_BARREL = BLOCKS.register("powder_barrel",
            () -> new PowderBarrel(Block.Properties.copy(Blocks.TERRACOTTA)));

    public static void init(){}


}
