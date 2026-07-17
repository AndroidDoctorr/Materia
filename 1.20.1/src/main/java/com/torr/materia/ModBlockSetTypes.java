package com.torr.materia;

import net.minecraft.world.level.block.state.properties.BlockSetType;

public class ModBlockSetTypes {
    public static final BlockSetType FIG = BlockSetType.register(new BlockSetType(materia.MOD_ID + ":fig"));
    public static final BlockSetType CEDAR = BlockSetType.register(new BlockSetType(materia.MOD_ID + ":cedar"));
    public static final BlockSetType EUCALYPTUS = BlockSetType.register(new BlockSetType(materia.MOD_ID + ":eucalyptus"));
    public static final BlockSetType RUBBER_WOOD = BlockSetType.register(new BlockSetType(materia.MOD_ID + ":rubber_wood"));
    public static final BlockSetType WROUGHT_IRON = BlockSetType.register(new BlockSetType(materia.MOD_ID + ":wrought_iron"));

    private ModBlockSetTypes() {
    }
}
