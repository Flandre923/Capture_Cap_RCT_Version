package com.gitlab.flandre923.rctcapturecap.neoforge;

import net.minecraft.world.level.GameRules;

public class ModCommonImpl {
    public static GameRules.Key<GameRules.IntegerValue> registerIntRule(String name, GameRules.Category category, int initialValue) {
        return GameRules.register(name, category, GameRules.IntegerValue.create(initialValue));
    }

    public static GameRules.Key<GameRules.BooleanValue> registerBooleanRule(String name, GameRules.Category category, boolean initialValue) {
        return GameRules.register(name, category, GameRules.BooleanValue.create(initialValue));
    }
}
