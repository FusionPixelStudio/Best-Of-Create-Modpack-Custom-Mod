package net.AsherRoland.BoCCustom.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.AsherRoland.BoCCustom.BocLang;
import net.AsherRoland.BoCCustom.blocks.recycling_block.RecyclingBlockSavedData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.UnknownNullability;

import java.util.function.Supplier;

import static net.minecraft.commands.Commands.literal;

public class recyclingReset {

    public recyclingReset(@UnknownNullability CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(literal("boc")
                .requires(cs -> cs.hasPermission(2))
                .then(literal("reset-recycling")
                        .executes((context) -> {
                            CommandSourceStack source = context.getSource();
                            ServerLevel level = source.getLevel();

                            RecyclingBlockSavedData data = RecyclingBlockSavedData.get(level);
                            data.resetTotalItemsRecycled(); // new method we'll add
                            RecyclingBlockSavedData.sendTotalRecycledToClient(level, 0);

                            BocLang.text("Recycling total reset!").sendChat(source.getPlayerOrException());
                            return 1;
                        })
                )
        );
    }
}
