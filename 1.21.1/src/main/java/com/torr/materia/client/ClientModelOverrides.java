package com.torr.materia.client;

import com.torr.materia.materia;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
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
    public static void registerAdditionalModels(ModelEvent.RegisterAdditional event) {
        event.register(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "item/flint_arrow")));
        event.register(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "item/steel_axe")));
        event.register(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "item/steel_pickaxe")));
        event.register(ModelResourceLocation.inventory(ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "item/steel_shovel")));
    }

    @SubscribeEvent
    public static void modifyBakedModels(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> models = event.getModels();

        // Vanilla item models are keyed by the item id + "inventory" variant.
        // Our replacement models live under assets/materia/models/item/*, so their model ids are materia:item/<name>.
        swap(models, ResourceLocation.fromNamespaceAndPath("minecraft", "arrow"), ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "item/flint_arrow"));
        swap(models, ResourceLocation.fromNamespaceAndPath("minecraft", "iron_axe"), ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "item/steel_axe"));
        swap(models, ResourceLocation.fromNamespaceAndPath("minecraft", "iron_pickaxe"), ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "item/steel_pickaxe"));
        swap(models, ResourceLocation.fromNamespaceAndPath("minecraft", "iron_shovel"), ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "item/steel_shovel"));
    }

    private static void swap(Map<ModelResourceLocation, BakedModel> models, ResourceLocation vanillaItemId, ResourceLocation replacementItemId) {
        ModelResourceLocation vanillaKey = new ModelResourceLocation(vanillaItemId, "inventory");
        ModelResourceLocation replacementKey = new ModelResourceLocation(replacementItemId, "inventory");

        BakedModel replacement = models.get(replacementKey);
        if (replacement != null) {
            models.put(vanillaKey, replacement);
        }
    }
}

