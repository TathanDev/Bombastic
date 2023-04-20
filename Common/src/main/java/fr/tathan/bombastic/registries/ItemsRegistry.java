package fr.tathan.bombastic.registries;

import fr.tathan.bombastic.Constants;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ItemsRegistry {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registry.ITEM, Constants.MODID);

    public static RegistryObject<Item> POWDER_BARREL = ITEMS.register("powder_barrel",
            () -> new BlockItem(BlocksRegistry.POWDER_BARREL.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));


    public static void init(){}
}
