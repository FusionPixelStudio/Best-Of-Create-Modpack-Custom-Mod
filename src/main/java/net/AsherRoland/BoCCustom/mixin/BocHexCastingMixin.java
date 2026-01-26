package net.AsherRoland.BoCCustom.mixin;

import at.petrak.hexcasting.common.msgs.MsgNewSpellPatternC2S;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import net.AsherRoland.BoCCustom.task.CastSpellTask;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MsgNewSpellPatternC2S.class)
public class BocHexCastingMixin {

    @Inject(method = "handle", at = @At("HEAD"))
    private void boc$onHexCast(MinecraftServer server, ServerPlayer player, CallbackInfo ci) {

        player.sendSystemMessage(
                Component.literal("§d[BoC] Hex spell cast detected")
        );

        if (!player.level().isClientSide) {
            castSpell(
                    (ServerPlayer) player,
                    "hex_fireball"
            );
        }

    }

    public static void castSpell(ServerPlayer player, String spellKey) {
        TeamData teamData = TeamData.get(player);

        if (teamData == null) return;

        for (Chapter chapter : teamData.getFile().getAllChapters()) {
            for (Quest quest : chapter.getQuests()) {
                if (!teamData.canStartTasks(quest)) continue;

                for (Task task : quest.getTasks()) {
                    if (!(task instanceof CastSpellTask castTask)) continue;

                    if (!castTask.getSpellKey().equals(spellKey)) continue;

                    castTask.progress(teamData, spellKey, 1, false);
                }
            }
        }
    }
}