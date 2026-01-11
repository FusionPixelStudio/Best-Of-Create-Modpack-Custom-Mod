package net.AsherRoland.BoCCustom.indexing;

import net.AsherRoland.BoCCustom.BocCustom;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BocCustom.MODID);

    public static final RegistryObject<CreativeModeTab> BEST_OF_CREATE_TAB = CREATIVE_MODE_TABS.register("best_of_create_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(Items.ACACIA_FENCE))
                    .title(Component.translatable("creativetab.best_of_create_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

//                        output.accept(ModItems.SAPPHIRE.get());
//                        output.accept(ModItems.RAW_SAPPHIRE.get());
//
//                        output.accept(ModItems.METAL_DETECTOR.get());
//                        output.accept(ModBlocks.SOUND_BLOCK.get());
//
//                        output.accept(ModBlocks.SAPPHIRE_BLOCK.get());
//                        output.accept(ModBlocks.RAW_SAPPHIRE_BLOCK.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
