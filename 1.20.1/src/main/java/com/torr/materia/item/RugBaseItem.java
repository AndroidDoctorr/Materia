package com.torr.materia.item;

import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;

/**
 * Plain woven backing for rugs. Extends {@link BannerItem} so the vanilla loom accepts it in the template slot.
 */
public class RugBaseItem extends BannerItem {
    public RugBaseItem(Item.Properties properties) {
        super(Blocks.WHITE_BANNER, Blocks.WHITE_WALL_BANNER, properties);
    }

    @Override
    public String getDescriptionId() {
        return "item.materia.rug_base";
    }
}
