package net.AsherRoland.BoCCustom.rendering;

import com.simibubi.create.Create;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;


public class AllPartialModels {

    public static final PartialModel

    RECYCLING_BLOCK = block("recycling_block"),
    RECYCLING_BLOCK_GRINDER = block("recycling_block_grinder"),
    RECYCLING_BLOCK_GRINDERLESS = block("recycling_block_grinderless");

    private static PartialModel block(String path) {
        return PartialModel.of(Create.asResource("block/" + path));
    }
}
