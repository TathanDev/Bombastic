package fr.tathan.bombastic.registries;

import fr.tathan.bombastic.Constants;
import fr.tathan.bombastic.entity.PrimedPowderBarrel;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class EntitiesRegistry {
    public static final RegistrationProvider<EntityType<?>> ENTITY_TYPES = RegistrationProvider.get(Registry.ENTITY_TYPE, Constants.MODID);

    public static RegistryObject<EntityType<PrimedPowderBarrel>> POWDER_BARREL = ENTITY_TYPES.register("powder_barrel",
            () -> EntityType.Builder.<PrimedPowderBarrel>of(PrimedPowderBarrel::new,
                    MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).clientTrackingRange(10).updateInterval(10).build("powder_barrel"));


    public static void init(){}

}
