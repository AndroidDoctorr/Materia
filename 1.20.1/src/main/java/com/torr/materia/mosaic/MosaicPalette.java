package com.torr.materia.mosaic;

/**
 * Ordered paint palette for mosaic blocks (27 entries).
 * Index 0 is unpainted (base block shows through). Indices 1–26 are paint colors.
 */
public final class MosaicPalette {
    public static final int UNPAINTED = 0;
    public static final int COUNT = 27;

    /** ARGB colors for client rendering (index matches stored pixel value). */
    private static final int[] COLORS = {
            0x00000000, // 0 unpainted — skipped when drawing
            0xFF1D1D21, // 1 black
            0xFFB02E26, // 2 red
            0xFFFED83D, // 3 yellow
            0xFF3C44AA, // 4 blue
            0xFF5E7C16, // 5 green
            0xFFF9801D, // 6 orange
            0xFFC74EBD, // 7 magenta
            0xFF3AB3DA, // 8 light blue
            0xFF80C71F, // 9 lime
            0xFFF38BAA, // 10 pink
            0xFF474F52, // 11 gray
            0xFF9D9D97, // 12 light gray
            0xFF169C9C, // 13 cyan
            0xFF8932B8, // 14 purple
            0xFF835432, // 15 brown
            0xFFC4914A, // 16 ochre
            0xFF9B4E2F, // 17 red ochre
            0xFFB19CD9, // 18 lavender
            0xFF4B0082, // 19 indigo
            0xFF66023C, // 20 tyrian purple
            0xFF8B8589, // 21 taupe
            0xFF6B8E23, // 22 olive
            0xFF36454F, // 23 charcoal gray
            0xFF800020, // 24 burgundy
            0xFF008080, // 25 teal
            0xFFD2B48C, // 26 tan
    };

    private MosaicPalette() {
    }

    public static int colorArgb(int index) {
        if (index < 0 || index >= COUNT) {
            return 0xFFFFFFFF;
        }
        return COLORS[index];
    }

    public static int cycleForward(int current) {
        return (current + 1) % COUNT;
    }

    public static int cycleBackward(int current) {
        return (current - 1 + COUNT) % COUNT;
    }
}
