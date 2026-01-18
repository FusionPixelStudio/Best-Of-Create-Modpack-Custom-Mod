package net.AsherRoland.BoCCustom.ponder;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.AsherRoland.BoCCustom.BocCustom;
import net.AsherRoland.BoCCustom.indexing.SoPRegistry;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

public class BocPonderTags {

    public static final ResourceLocation TCONSTRUCT = new ResourceLocation(BocCustom.MODID, "tconstruct");
    public static final ResourceLocation BOC = new ResourceLocation(BocCustom.MODID, "boc");

    public BocPonderTags() {}

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.registerTag(BOC)
                .addToIndex()
                .item(SoPRegistry.RECYCLING_BLOCK, true, true)
                .register();

        HELPER.registerTag(TCONSTRUCT)
                .addToIndex()
                .item(TinkerSmeltery.searedMelter.get(), true, true)
                .register();


        PonderTagRegistrationHelper<ItemLike> HELPER2 =
                helper.withKeyFunction(item -> BuiltInRegistries.ITEM.getKey(item.asItem()));


        HELPER2.addToTag(TCONSTRUCT)
                .add(TinkerSmeltery.searedHeater)
                .add(TinkerSmeltery.searedBasin)
                .add(TinkerSmeltery.searedTable)
                .add(TinkerSmeltery.searedFaucet)
                .add(TinkerSmeltery.searedBricks);
    }
}
