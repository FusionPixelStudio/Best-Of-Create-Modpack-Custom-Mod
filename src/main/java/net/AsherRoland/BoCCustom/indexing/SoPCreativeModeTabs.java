package net.AsherRoland.BoCCustom.indexing;

import com.tterrag.registrate.util.entry.RegistryEntry;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import net.AsherRoland.BoCCustom.BocCustom;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class SoPCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BocCustom.MODID);

    public static final RegistryObject<CreativeModeTab> BEST_OF_CREATE =
            CREATIVE_TABS.register("best_of_create", () ->
                    CreativeModeTab.builder()
                            .title(Component.literal("Best of Create"))
                            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                            .icon(SoPRegistry.RECYCLING_BLOCK::asStack)
                            .displayItems(new RegistrateDisplayItemsGenerator())
                            .build()
            );

    public static void register(IEventBus bus) {
        CREATIVE_TABS.register(bus);
    }

    public static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        private List<Item> collectBlocks(RegistryObject<CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();
            for (RegistryEntry<Block> entry : SoPRegistry.REGISTRATE.getAll(Registries.BLOCK)) {
                if (!SoPRegistry.REGISTRATE.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get()
                        .asItem();
                if (item == Items.AIR)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            items = new ReferenceArrayList<>(new ReferenceLinkedOpenHashSet<>(items));
            return items;
        }

        private List<Item> collectItems(RegistryObject<CreativeModeTab> tab, Predicate<Item> exclusionPredicate) {
            List<Item> items = new ReferenceArrayList<>();


            for (RegistryEntry<Item> entry : SoPRegistry.REGISTRATE.getAll(Registries.ITEM)) {
                if (!SoPRegistry.REGISTRATE.isInCreativeTab(entry, tab))
                    continue;
                Item item = entry.get();
                if (item instanceof BlockItem)
                    continue;
                if (!exclusionPredicate.test(item))
                    items.add(item);
            }
            return items;
        }

        private static void outputAll(CreativeModeTab.Output output, List<Item> items) {
            for (Item item : items) {
                output.accept(item);
            }
        }

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters params,
                           CreativeModeTab.Output output) {

            // Exclusion predicate: return TRUE to exclude
            Predicate<Item> exclusionPredicate = item -> {
                // Example conditional exclusion
//                if (item == SoPAllBlocks.DIGITAL_ADAPTER.asItem())
//                    return !CreateAddition.CC_ACTIVE;

                return false; // do not exclude by default
            };

            List<Item> items = new LinkedList<>();

            // IMPORTANT: pass the actual tab RegistryObject
            items.addAll(collectBlocks(BEST_OF_CREATE, exclusionPredicate));
            items.addAll(collectItems(BEST_OF_CREATE, exclusionPredicate));

            outputAll(output, items);
        }
    }



}
