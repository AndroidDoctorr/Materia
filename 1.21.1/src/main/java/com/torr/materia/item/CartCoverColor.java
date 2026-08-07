package com.torr.materia.item;

import com.torr.materia.materia;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Cart cover dye variants — one item and entity texture per color. */
public enum CartCoverColor {
    WHITE("white", "White"),
    ORANGE("orange", "Orange"),
    MAGENTA("magenta", "Magenta"),
    LIGHT_BLUE("light_blue", "Light Blue"),
    YELLOW("yellow", "Yellow"),
    LIME("lime", "Lime"),
    PINK("pink", "Pink"),
    GRAY("gray", "Gray"),
    LIGHT_GRAY("light_gray", "Light Gray"),
    CYAN("cyan", "Cyan"),
    PURPLE("purple", "Purple"),
    BLUE("blue", "Blue"),
    BROWN("brown", "Brown"),
    GREEN("green", "Green"),
    RED("red", "Red"),
    BLACK("black", "Black"),
    OCHRE("ochre", "Ochre"),
    RED_OCHRE("red_ochre", "Red Ochre"),
    INDIGO("indigo", "Indigo"),
    OLIVE("olive", "Olive"),
    TEAL("teal", "Teal"),
    TYRIAN_PURPLE("tyrian_purple", "Tyrian Purple"),
    LAVENDER("lavender", "Lavender"),
    CHARCOAL_GRAY("charcoal_gray", "Charcoal Gray"),
    TAUPE("taupe", "Taupe"),
    BURGUNDY("burgundy", "Burgundy"),
    TAN("tan", "Tan");

    private static final Map<String, CartCoverColor> BY_ID = Arrays.stream(values())
            .collect(Collectors.toMap(CartCoverColor::getId, Function.identity()));

    private final String id;
    private final String displayName;

    CartCoverColor(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return this.id;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getItemId() {
        return this.id + "_cart_cover";
    }

    public String getBlanketItemId() {
        return this.id + "_blanket";
    }

    public ResourceLocation getEntityTexture() {
        return ResourceLocation.fromNamespaceAndPath(materia.MOD_ID, "textures/entity/" + this.id + "_cart_cover.png");
    }

    /** Synced entity data: 0 = none, 1..N = variant. */
    public int networkId() {
        return this.ordinal() + 1;
    }

    public static Optional<CartCoverColor> fromNetworkId(int id) {
        if (id <= 0 || id > values().length) {
            return Optional.empty();
        }
        return Optional.of(values()[id - 1]);
    }

    public static Optional<CartCoverColor> fromId(String id) {
        if (id == null || id.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(BY_ID.get(id.toLowerCase(Locale.ROOT)));
    }
}
