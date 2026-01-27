package net.AsherRoland.BoCCustom.ponder;

import com.quintonc.vs_sails.registration.SailsBlocks;
import net.AsherRoland.BoCCustom.indexing.SoPRegistry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

import static net.AsherRoland.BoCCustom.ponder.BocPonderTags.*;

public class BocPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemLike> HELPER =
                helper.withKeyFunction(item ->
                        BuiltInRegistries.ITEM.getKey(item.asItem())
                );

        HELPER.addStoryBoard(TinkerSmeltery.searedMelter.get(), "first_smelter_example", PonderScenes::searedMelter, TCONSTRUCT);

        HELPER.addStoryBoard(SoPRegistry.RECYCLING_BLOCK.get(), "recycler_example", PonderScenes::recyclingBlock, BOC);

        HELPER.addStoryBoard(SailsBlocks.OAK_HELM.get(), "valk_sails_ponder_example_1", PonderScenes::firstShip, VALKSAILS);
    }
}
