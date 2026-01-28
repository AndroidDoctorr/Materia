package com.torr.materia.entity;

import com.torr.materia.ModItems;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

/**
 * Custom sheep colors beyond the vanilla 16 DyeColor values
 */
public enum CustomSheepColor {
    // Vanilla colors (for compatibility)
    WHITE(DyeColor.WHITE, ModItems.CLUMP_OF_WHITE_WOOL, 0xF9FFFE),
    ORANGE(DyeColor.ORANGE, ModItems.CLUMP_OF_ORANGE_WOOL, 0xF9801D),
    MAGENTA(DyeColor.MAGENTA, ModItems.CLUMP_OF_MAGENTA_WOOL, 0xC74EBD),
    LIGHT_BLUE(DyeColor.LIGHT_BLUE, ModItems.CLUMP_OF_LIGHT_BLUE_WOOL, 0x3AB3DA),
    YELLOW(DyeColor.YELLOW, ModItems.CLUMP_OF_YELLOW_WOOL, 0xFED83D),
    LIME(DyeColor.LIME, ModItems.CLUMP_OF_LIME_WOOL, 0x80C71F),
    PINK(DyeColor.PINK, ModItems.CLUMP_OF_PINK_WOOL, 0xF38BAA),
    GRAY(DyeColor.GRAY, ModItems.CLUMP_OF_GRAY_WOOL, 0x474F52),
    LIGHT_GRAY(DyeColor.LIGHT_GRAY, ModItems.CLUMP_OF_LIGHT_GRAY_WOOL, 0x9D9D97),
    CYAN(DyeColor.CYAN, ModItems.CLUMP_OF_CYAN_WOOL, 0x169C9C),
    PURPLE(DyeColor.PURPLE, ModItems.CLUMP_OF_PURPLE_WOOL, 0x8932B8),
    BLUE(DyeColor.BLUE, ModItems.CLUMP_OF_BLUE_WOOL, 0x3C44AA),
    BROWN(DyeColor.BROWN, ModItems.CLUMP_OF_BROWN_WOOL, 0x835432),
    GREEN(DyeColor.GREEN, ModItems.CLUMP_OF_GREEN_WOOL, 0x5E7C16),
    RED(DyeColor.RED, ModItems.CLUMP_OF_RED_WOOL, 0xB02E26),
    BLACK(DyeColor.BLACK, ModItems.CLUMP_OF_BLACK_WOOL, 0x1D1D21),
    
    // Custom colors
    OCHRE(null, ModItems.CLUMP_OF_OCHRE_WOOL, 0xB87E14),
    RED_OCHRE(null, ModItems.CLUMP_OF_RED_OCHRE_WOOL, 0xB84018),
    INDIGO(null, ModItems.CLUMP_OF_INDIGO_WOOL, 0x4D3CB0),
    OLIVE(null, ModItems.CLUMP_OF_OLIVE_WOOL, 0x808000),
    TYRIAN_PURPLE(null, ModItems.CLUMP_OF_TYRIAN_PURPLE_WOOL, 0x66023C),
    LAVENDER(null, ModItems.CLUMP_OF_LAVENDER_WOOL, 0xB19CD9),
    CHARCOAL_GRAY(null, ModItems.CLUMP_OF_CHARCOAL_GRAY_WOOL, 0x363332),
    TAUPE(null, ModItems.CLUMP_OF_WOOL, 0xCFC1B0);
    
    private final DyeColor vanillaEquivalent;
    private final java.util.function.Supplier<Item> clumpItem;
    private final int color;
    
    CustomSheepColor(DyeColor vanillaEquivalent, java.util.function.Supplier<Item> clumpItem, int color) {
        this.vanillaEquivalent = vanillaEquivalent;
        this.clumpItem = clumpItem;
        this.color = color;
    }
    
    public DyeColor getVanillaEquivalent() {
        return vanillaEquivalent;
    }
    
    public Item getClumpItem() {
        return clumpItem.get();
    }
    
    public int getColor() {
        return color;
    }
    
    public boolean isCustomColor() {
        return vanillaEquivalent == null;
    }
    
    /**
     * Get CustomSheepColor from vanilla DyeColor
     */
    public static CustomSheepColor fromVanilla(DyeColor dyeColor) {
        for (CustomSheepColor color : values()) {
            if (color.vanillaEquivalent == dyeColor) {
                return color;
            }
        }
        return WHITE; // fallback
    }
    
    /**
     * Get CustomSheepColor from dye item
     */
    public static CustomSheepColor fromDyeItem(Item dyeItem) {
        // Check custom dyes first
        if (dyeItem == ModItems.OCHRE.get()) return OCHRE;
        if (dyeItem == ModItems.RED_OCHRE.get()) return RED_OCHRE;
        if (dyeItem == ModItems.INDIGO_DYE.get()) return INDIGO;
        if (dyeItem == ModItems.OLIVE_DYE.get()) return OLIVE;
        if (dyeItem == ModItems.TYRIAN_PURPLE_DYE.get()) return TYRIAN_PURPLE;
        if (dyeItem == ModItems.LAVENDER_DYE.get()) return LAVENDER;
        if (dyeItem == ModItems.CHARCOAL_GRAY_DYE.get()) return CHARCOAL_GRAY;
        if (dyeItem == ModItems.TAUPE_DYE.get()) return TAUPE;
        
        // Check vanilla dyes manually (1.18.2 doesn't have getColorFromItem)
        DyeColor vanillaColor = getVanillaDyeColor(dyeItem);
        if (vanillaColor != null) {
            return fromVanilla(vanillaColor);
        }
        
        return null; // Not a recognized dye
    }
    
    /**
     * Manual vanilla dye detection for 1.18.2 compatibility
     */
    private static DyeColor getVanillaDyeColor(Item dyeItem) {
        if (dyeItem == net.minecraft.world.item.Items.WHITE_DYE) return DyeColor.WHITE;
        if (dyeItem == net.minecraft.world.item.Items.ORANGE_DYE) return DyeColor.ORANGE;
        if (dyeItem == net.minecraft.world.item.Items.MAGENTA_DYE) return DyeColor.MAGENTA;
        if (dyeItem == net.minecraft.world.item.Items.LIGHT_BLUE_DYE) return DyeColor.LIGHT_BLUE;
        if (dyeItem == net.minecraft.world.item.Items.YELLOW_DYE) return DyeColor.YELLOW;
        if (dyeItem == net.minecraft.world.item.Items.LIME_DYE) return DyeColor.LIME;
        if (dyeItem == net.minecraft.world.item.Items.PINK_DYE) return DyeColor.PINK;
        if (dyeItem == net.minecraft.world.item.Items.GRAY_DYE) return DyeColor.GRAY;
        if (dyeItem == net.minecraft.world.item.Items.LIGHT_GRAY_DYE) return DyeColor.LIGHT_GRAY;
        if (dyeItem == net.minecraft.world.item.Items.CYAN_DYE) return DyeColor.CYAN;
        if (dyeItem == net.minecraft.world.item.Items.PURPLE_DYE) return DyeColor.PURPLE;
        if (dyeItem == net.minecraft.world.item.Items.BLUE_DYE) return DyeColor.BLUE;
        if (dyeItem == net.minecraft.world.item.Items.BROWN_DYE) return DyeColor.BROWN;
        if (dyeItem == net.minecraft.world.item.Items.GREEN_DYE) return DyeColor.GREEN;
        if (dyeItem == net.minecraft.world.item.Items.RED_DYE) return DyeColor.RED;
        if (dyeItem == net.minecraft.world.item.Items.BLACK_DYE) return DyeColor.BLACK;
        
        return null; // Not a vanilla dye
    }
}
