package fr.tathan.bombastic;

import fr.tathan.bombastic.CommonClass;
import fr.tathan.bombastic.Constants;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.renderer.entity.TntRenderer;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.TntBlock;

public class Bombastic implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();

        
    }


}
