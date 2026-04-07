package com.eightsidedsquare.euphorics.common.util;

import net.fabricmc.loader.api.*;
import java.util.*;
import org.objectweb.asm.tree.*;
import org.spongepowered.asm.mixin.extensibility.*;

public class MixinPlugin implements IMixinConfigPlugin
{
    public boolean shouldApplyMixin(final String targetClassName, final String mixinClassName) {
        if (mixinClassName.contains("compat.")) {
            final String[] mixinString = mixinClassName.split("\\.");
            return FabricLoader.getInstance().isModLoaded(mixinString[mixinString.length - 1].split("\\$")[0].toLowerCase());
        }
        return true;
    }

    public void onLoad(final String mixinPackage) {
    }

    public String getRefMapperConfig() {
        return null;
    }

    public void acceptTargets(final Set<String> myTargets, final Set<String> otherTargets) {
    }

    public List<String> getMixins() {
        return null;
    }

    public void preApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {
    }

    public void postApply(final String targetClassName, final ClassNode targetClass, final String mixinClassName, final IMixinInfo mixinInfo) {
    }
}
