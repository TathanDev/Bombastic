package fr.tathan.bombastic;


import fr.tathan.bombastic.entity.renderer.MinecartPowderBarrelRenderer;
import fr.tathan.bombastic.entity.renderer.PowderBarrelRenderer;
import fr.tathan.bombastic.registries.EntitiesRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.level.block.TntBlock;

public class BombasticClient implements ClientModInitializer  {

    @Override
    public void onInitializeClient() {

        EntityRendererRegistry.register(EntitiesRegistry.POWDER_BARREL.get(), PowderBarrelRenderer::new);
        EntityRendererRegistry.register(EntitiesRegistry.TNT_MINECART.get(), MinecartPowderBarrelRenderer::new);

    }


}
