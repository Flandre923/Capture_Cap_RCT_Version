/*
 * This file is part of Radical Cobblemon Trainers.
 * Copyright (c) 2025, HDainester, All rights reserved.
 *
 * Radical Cobblemon Trainers is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Radical Cobblemon Trainers is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with Radical Cobblemon Trainers. If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package com.gitlab.flandre923.rctcapturecap;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModCommon {
    public static final String MOD_ID = "rctcapturecap";
    public static final String MOD_NAME = "Capture Cap - RCT Version";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static final GameRules.Key<GameRules.BooleanValue> SHOW_LEVEL_CAP_MESSAGES;
    public static final GameRules.Key<GameRules.IntegerValue> LEVEL_CAP;
    public static final GameRules.Key<GameRules.BooleanValue> BYPASS_SHINY;
    public static final GameRules.Key<GameRules.BooleanValue> BYPASS_MASTER_BALL;
    static {
        LEVEL_CAP = registerIntRule("levelCap", GameRules.Category.MISC, 0);
        SHOW_LEVEL_CAP_MESSAGES = registerBooleanRule("showLevelCapMessages", GameRules.Category.MISC, true);
        BYPASS_SHINY = registerBooleanRule("bypassShiny", GameRules.Category.MISC, true);
        BYPASS_MASTER_BALL = registerBooleanRule("bypassMasterBall", GameRules.Category.MISC, true);
    }
    public static void init() {
//        ModItems.register();
    }

    @ExpectPlatform
    public static GameRules.Key<GameRules.IntegerValue> registerIntRule(String name, GameRules.Category category, int initialValue) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static GameRules.Key<GameRules.BooleanValue> registerBooleanRule(String name, GameRules.Category category, boolean initialValue) {
        throw new AssertionError();
    }
}
