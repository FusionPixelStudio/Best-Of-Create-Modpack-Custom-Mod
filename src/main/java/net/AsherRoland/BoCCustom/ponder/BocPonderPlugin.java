package net.AsherRoland.BoCCustom.ponder;

import net.AsherRoland.BoCCustom.BocCustom;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class BocPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return BocCustom.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        BocPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        BocPonderTags.register(helper);
    }
}
