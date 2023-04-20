package fr.tathan.bombastic;

import fr.tathan.bombastic.entity.renderer.PowderBarrelRenderer;
import fr.tathan.bombastic.registries.EntitiesRegistry;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
@Mod(Constants.MODID)

public class Bombastic {

    public Bombastic() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        Constants.LOG.info("Bombastic");
        CommonClass.init();

    }

    @Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            EntityRenderers.register(EntitiesRegistry.POWDER_BARREL.get(), PowderBarrelRenderer::new);
        }
    }



}
