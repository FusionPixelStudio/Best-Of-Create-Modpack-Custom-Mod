package net.AsherRoland.BoCCustom.indexing;

import net.AsherRoland.BoCCustom.BocCustom;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SoPCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BocCustom.MODID);

    public static final RegistryObject<CreativeModeTab> BEST_OF_CREATE =
            CREATIVE_TABS.register("best_of_create", () ->
                    CreativeModeTab.builder()
                            .title(Component.literal("Best of Create"))
                            .icon(() -> new ItemStack(SoPAllBlocks.RECYCLING_BLOCK.asItem()))
                            .displayItems((params, output) -> {
                                output.accept(SoPAllBlocks.RECYCLING_BLOCK.asItem());
                            })
                            .build()
            );

    public static void register(IEventBus bus) {
        CREATIVE_TABS.register(bus);
    }

}
