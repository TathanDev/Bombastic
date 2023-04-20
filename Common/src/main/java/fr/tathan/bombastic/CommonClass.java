package fr.tathan.bombastic;

import fr.tathan.bombastic.platform.Services;
import fr.tathan.bombastic.registries.BlocksRegistry;
import fr.tathan.bombastic.registries.EntitiesRegistry;
import fr.tathan.bombastic.registries.ItemsRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CommonClass {


    public static void init() {

        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.isDevelopmentEnvironment() ? "development" : "production");
        Constants.LOG.info("Diamond Item >> {}", Registry.ITEM.getKey(Items.DIAMOND));

        ItemsRegistry.init();
        BlocksRegistry.init();
        EntitiesRegistry.init();
    }



}