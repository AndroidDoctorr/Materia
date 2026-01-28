package com.torr.materia.client;

import com.torr.materia.materia;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

/**
 * Forge-supported way to override vanilla item models from a mod in 1.18.2.
 *
 * We load our replacement models under the materia namespace and then swap the baked model
 * for the vanilla items (minecraft:arrow, minecraft:iron_axe, etc).
 */
@Mod.EventBusSubscriber(modid = materia.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModelOverrides {

    @SubscribeEvent
    public static void registerAdditionalModels(ModelRegistryEvent event) {
        ForgeModelBakery.addSpecialModel(new ResourceLocation(materia.MOD_ID, "item/flint_arrow"));
        ForgeModelBakery.addSpecialModel(new ResourceLocation(materia.MOD_ID, "item/steel_axe"));
        ForgeModelBakery.addSpecialModel(new ResourceLocation(materia.MOD_ID, "item/steel_pickaxe"));
        ForgeModelBakery.addSpecialModel(new ResourceLocation(materia.MOD_ID, "item/steel_shovel"));
    }

    @SubscribeEvent
    public static void modifyBakedModels(ModelBakeEvent event) {
        Map<ResourceLocation, BakedModel> models = event.getModelRegistry();

        // Vanilla item models are keyed by the item id + "inventory" variant.
        // Our replacement models live under assets/materia/models/item/*, so their model ids are materia:item/<name>.
        swap(models, new ResourceLocation("minecraft", "arrow"), new ResourceLocation(materia.MOD_ID, "item/flint_arrow"));
        swap(models, new ResourceLocation("minecraft", "iron_axe"), new ResourceLocation(materia.MOD_ID, "item/steel_axe"));
        swap(models, new ResourceLocation("minecraft", "iron_pickaxe"), new ResourceLocation(materia.MOD_ID, "item/steel_pickaxe"));
        swap(models, new ResourceLocation("minecraft", "iron_shovel"), new ResourceLocation(materia.MOD_ID, "item/steel_shovel"));
    }

    private static void swap(Map<ResourceLocation, BakedModel> models, ResourceLocation vanillaItemId, ResourceLocation replacementItemId) {
        ModelResourceLocation vanillaKey = new ModelResourceLocation(vanillaItemId, "inventory");
        ModelResourceLocation replacementKey = new ModelResourceLocation(replacementItemId, "inventory");

        BakedModel replacement = models.get(replacementKey);
        if (replacement != null) {
            models.put(vanillaKey, replacement);
        }
    }
}

