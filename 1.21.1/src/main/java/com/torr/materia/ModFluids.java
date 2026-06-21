package com.torr.materia;

import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Forge {@link FluidType} + flowing pair for amphora liquids so pipes can fill/drain alongside bottles.
 * No buckets or world fluid blocks — capability-only.
 */
public final class ModFluids {

    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, materia.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(ForgeRegistries.FLUIDS, materia.MOD_ID);

    public static final RegistryObject<FluidType> WINE_TYPE = registerType("wine", 1024, 1200, 300);
    public static final RegistryObject<FluidType> GRAPE_JUICE_TYPE = registerType("grape_juice", 1030, 1100, 300);
    public static final RegistryObject<FluidType> OLIVE_OIL_TYPE = registerType("olive_oil", 920, 5000, 300);
    public static final RegistryObject<FluidType> VINEGAR_TYPE = registerType("vinegar", 1010, 1500, 300);
    public static final RegistryObject<FluidType> BEER_TYPE = registerType("beer", 1010, 2000, 285);
    public static final RegistryObject<FluidType> BEER_MASH_TYPE = registerType("beer_mash", 1050, 3500, 300);
    public static final RegistryObject<FluidType> TEA_TYPE = registerType("tea", 1005, 1800, 320);

    private static ForgeFlowingFluid.Properties wineProps;
    private static ForgeFlowingFluid.Properties grapeJuiceProps;
    private static ForgeFlowingFluid.Properties oliveOilProps;
    private static ForgeFlowingFluid.Properties vinegarProps;
    private static ForgeFlowingFluid.Properties beerProps;
    private static ForgeFlowingFluid.Properties beerMashProps;
    private static ForgeFlowingFluid.Properties teaProps;

    public static final RegistryObject<FlowingFluid> WINE_FLOWING = FLUIDS.register("wine_flowing",
            () -> new ForgeFlowingFluid.Flowing(wineProps()));
    public static final RegistryObject<FlowingFluid> WINE_STILL = FLUIDS.register("wine",
            () -> new ForgeFlowingFluid.Source(wineProps()));

    public static final RegistryObject<FlowingFluid> GRAPE_JUICE_FLOWING = FLUIDS.register("grape_juice_flowing",
            () -> new ForgeFlowingFluid.Flowing(grapeJuiceProps()));
    public static final RegistryObject<FlowingFluid> GRAPE_JUICE_STILL = FLUIDS.register("grape_juice",
            () -> new ForgeFlowingFluid.Source(grapeJuiceProps()));

    public static final RegistryObject<FlowingFluid> OLIVE_OIL_FLOWING = FLUIDS.register("olive_oil_flowing",
            () -> new ForgeFlowingFluid.Flowing(oliveOilProps()));
    public static final RegistryObject<FlowingFluid> OLIVE_OIL_STILL = FLUIDS.register("olive_oil",
            () -> new ForgeFlowingFluid.Source(oliveOilProps()));

    public static final RegistryObject<FlowingFluid> VINEGAR_FLOWING = FLUIDS.register("vinegar_flowing",
            () -> new ForgeFlowingFluid.Flowing(vinegarProps()));
    public static final RegistryObject<FlowingFluid> VINEGAR_STILL = FLUIDS.register("vinegar",
            () -> new ForgeFlowingFluid.Source(vinegarProps()));

    public static final RegistryObject<FlowingFluid> BEER_FLOWING = FLUIDS.register("beer_flowing",
            () -> new ForgeFlowingFluid.Flowing(beerProps()));
    public static final RegistryObject<FlowingFluid> BEER_STILL = FLUIDS.register("beer",
            () -> new ForgeFlowingFluid.Source(beerProps()));

    public static final RegistryObject<FlowingFluid> BEER_MASH_FLOWING = FLUIDS.register("beer_mash_flowing",
            () -> new ForgeFlowingFluid.Flowing(beerMashProps()));
    public static final RegistryObject<FlowingFluid> BEER_MASH_STILL = FLUIDS.register("beer_mash",
            () -> new ForgeFlowingFluid.Source(beerMashProps()));

    public static final RegistryObject<FlowingFluid> TEA_FLOWING = FLUIDS.register("tea_flowing",
            () -> new ForgeFlowingFluid.Flowing(teaProps()));
    public static final RegistryObject<FlowingFluid> TEA_STILL = FLUIDS.register("tea",
            () -> new ForgeFlowingFluid.Source(teaProps()));

    private static ForgeFlowingFluid.Properties wineProps() {
        if (wineProps == null) {
            wineProps = new ForgeFlowingFluid.Properties(WINE_TYPE::get, WINE_STILL::get, WINE_FLOWING::get);
        }
        return wineProps;
    }

    private static ForgeFlowingFluid.Properties grapeJuiceProps() {
        if (grapeJuiceProps == null) {
            grapeJuiceProps = new ForgeFlowingFluid.Properties(GRAPE_JUICE_TYPE::get, GRAPE_JUICE_STILL::get, GRAPE_JUICE_FLOWING::get);
        }
        return grapeJuiceProps;
    }

    private static ForgeFlowingFluid.Properties oliveOilProps() {
        if (oliveOilProps == null) {
            oliveOilProps = new ForgeFlowingFluid.Properties(OLIVE_OIL_TYPE::get, OLIVE_OIL_STILL::get, OLIVE_OIL_FLOWING::get);
        }
        return oliveOilProps;
    }

    private static ForgeFlowingFluid.Properties vinegarProps() {
        if (vinegarProps == null) {
            vinegarProps = new ForgeFlowingFluid.Properties(VINEGAR_TYPE::get, VINEGAR_STILL::get, VINEGAR_FLOWING::get);
        }
        return vinegarProps;
    }

    private static ForgeFlowingFluid.Properties beerProps() {
        if (beerProps == null) {
            beerProps = new ForgeFlowingFluid.Properties(BEER_TYPE::get, BEER_STILL::get, BEER_FLOWING::get);
        }
        return beerProps;
    }

    private static ForgeFlowingFluid.Properties beerMashProps() {
        if (beerMashProps == null) {
            beerMashProps = new ForgeFlowingFluid.Properties(BEER_MASH_TYPE::get, BEER_MASH_STILL::get, BEER_MASH_FLOWING::get);
        }
        return beerMashProps;
    }

    private static RegistryObject<FluidType> registerType(String name, int density, int viscosity, int temperature) {
        return FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create()
                .descriptionId("fluid_type." + materia.MOD_ID + "." + name)
                .density(density)
                .viscosity(viscosity)
                .temperature(temperature)));
    }

    public static void register(net.minecraftforge.eventbus.api.IEventBus bus) {
        FLUID_TYPES.register(bus);
        FLUIDS.register(bus);
    }


    private static ForgeFlowingFluid.Properties teaProps() {
        if (teaProps == null) {
            teaProps = new ForgeFlowingFluid.Properties(TEA_TYPE::get, TEA_STILL::get, TEA_FLOWING::get);
        }
        return teaProps;
    }

    private ModFluids() {}
}
