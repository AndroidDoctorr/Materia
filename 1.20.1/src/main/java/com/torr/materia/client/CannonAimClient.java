package com.torr.materia.client;

import com.torr.materia.client.screen.CannonAimScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class CannonAimClient {
    private CannonAimClient() {
    }

    public static void open(BlockPos pos) {
        Minecraft.getInstance().setScreen(new CannonAimScreen(pos));
    }
}

