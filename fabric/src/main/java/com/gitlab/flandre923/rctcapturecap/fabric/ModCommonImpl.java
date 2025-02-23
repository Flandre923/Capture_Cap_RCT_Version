package com.gitlab.flandre923.rctcapturecap.fabric;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class ModCommonImpl {
    public static GameRules.Key<GameRules.IntegerValue> registerIntRule(String name, GameRules.Category category, int initialValue) {
        return GameRuleRegistry.register(name, category, GameRuleFactory.createIntRule(initialValue));
    }

    public static GameRules.Key<GameRules.BooleanValue> registerBooleanRule(String name, GameRules.Category category, boolean initialValue) {
        return GameRuleRegistry.register(name, category, GameRuleFactory.createBooleanRule(initialValue));
    }
}
