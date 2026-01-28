package com.torr.materia.datagen;

import com.torr.materia.materia;
import com.torr.materia.datagen.worldgen.ModWorldgenProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public class ModDataGenerators {
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModWorldgenProvider(output, lookupProvider));
    }
}

