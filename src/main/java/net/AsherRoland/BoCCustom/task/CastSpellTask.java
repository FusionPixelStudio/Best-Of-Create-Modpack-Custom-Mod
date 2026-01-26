package net.AsherRoland.BoCCustom.task;

import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import dev.ftb.mods.ftbquests.quest.task.TaskType;
import net.AsherRoland.BoCCustom.BocCustom;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CastSpellTask extends Task{

    private String spellKey = "";

    public String getSpellKey() {
        return spellKey;
    }

    public CastSpellTask(long id, Quest quest) {
        super(id, quest);
    }

    @Override
    public TaskType getType() {
        return BocCustom.HEX_CASTING;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public MutableComponent getAltTitle() {
        return Component.literal("Cast Pattern");
    }

    @Override
    public void readData(CompoundTag tag) {
        super.readData(tag);
        spellKey = tag.getString("spell");
    }

    @Override
    public void writeData(CompoundTag tag) {
        super.writeData(tag);
        tag.putString("spell", spellKey);
    }

    public void progress(TeamData teamData, String key, long value, boolean ignore) {
        if (!teamData.isCompleted(this)) {
            if (ignore) {
                teamData.addProgress(this, value);
            } else if (checkTaskSequence(teamData) && teamData.canStartTasks(getQuest())) {
                teamData.addProgress(this, value);
            }
        }
    }
}
