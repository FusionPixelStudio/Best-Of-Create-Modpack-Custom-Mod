package net.AsherRoland.BoCCustom.rendering;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.AsherRoland.BoCCustom.BocCustom;
import net.minecraft.resources.ResourceLocation;


public class AllPartialModels {

    public static final PartialModel RECYCLER_GRINDER = block("block/recycling_block_grinder");

    private static PartialModel block(String path) {
        return PartialModel.of(new ResourceLocation(BocCustom.MODID, "block/" + path));
    }

    public static void init() {
        // init static fields
    }
}
