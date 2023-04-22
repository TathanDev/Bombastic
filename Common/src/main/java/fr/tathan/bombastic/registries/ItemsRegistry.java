package fr.tathan.bombastic.registries;

import fr.tathan.bombastic.Constants;
import fr.tathan.bombastic.items.CustomMinecartItem;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;

public class ItemsRegistry {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registry.ITEM, Constants.MODID);

    public static RegistryObject<Item> POWDER_BARREL = ITEMS.register("powder_barrel",
            () -> new BlockItem(BlocksRegistry.POWDER_BARREL.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));

    public static final RegistryObject<Item> TNT_MINECART = ITEMS.register("powder_barrel_minecart",
            () -> new CustomMinecartItem(EntitiesRegistry.TNT_MINECART.get(), (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION)));


    public static void init(){}
}
