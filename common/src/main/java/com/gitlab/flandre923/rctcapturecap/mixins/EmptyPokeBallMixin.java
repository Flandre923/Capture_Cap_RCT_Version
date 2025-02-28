package com.gitlab.flandre923.rctcapturecap.mixins;

import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.ActiveBattlePokemon;
import com.cobblemon.mod.common.battles.BattleCaptureAction;
import com.cobblemon.mod.common.entity.pokeball.EmptyPokeBallEntity;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.battle.BattleCaptureEndPacket;
import com.cobblemon.mod.common.util.PlayerExtensionsKt;
import com.cobblemon.mod.common.util.WorldExtensionsKt;
import com.gitlab.flandre923.rctcapturecap.LevelCapGetHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.gitlab.flandre923.rctcapturecap.ModCommon.LEVEL_CAP;
import static com.gitlab.flandre923.rctcapturecap.ModCommon.SHOW_LEVEL_CAP_MESSAGES;

@Mixin(EmptyPokeBallEntity.class)
public abstract class EmptyPokeBallMixin {

    @Shadow
    private PokemonEntity capturingPokemon;

    @Shadow protected abstract void drop();

    @Inject(method = "onHitEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/cobblemon/mod/common/entity/pokeball/EmptyPokeBallEntity;attemptCatch(Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;)V",
                    shift = At.Shift.BEFORE,
                    ordinal = 0
            ),
            cancellable = true)
    private void applyLevelCap(EntityHitResult hitResult, CallbackInfo ci) {
        EmptyPokeBallEntity self = (EmptyPokeBallEntity) (Object) this;

        if (self.getOwner() instanceof ServerPlayer player) {
            if (!player.isCreative() && !self.getPokeBall().getCatchRateModifier().isGuaranteed() && !capturingPokemon.getPokemon().getShiny()) {

                int playerLevel = LevelCapGetHelper.getLevelCap(player);
                int targetLevel = capturingPokemon.getPokemon().getLevel();

                GameRules gamerules = self.level().getGameRules();
                int levelCap = gamerules.getInt(LEVEL_CAP);
                boolean showMessages = gamerules.getBoolean(SHOW_LEVEL_CAP_MESSAGES);

                if (targetLevel > playerLevel + levelCap) {
                    if(capturingPokemon.isBattling()){
                        PokemonBattle battle = PlayerExtensionsKt.getBattleState(player).component1();
                        ActiveBattlePokemon hitBattlePokemon = null;
                        if(battle != null) {
                            for (ActiveBattlePokemon battlePokemon : battle.getActor(player).getSide().getOppositeSide().getActivePokemon()) {
                                if (battlePokemon.getBattlePokemon().getEffectedPokemon().getEntity() == capturingPokemon) {
                                    hitBattlePokemon = battlePokemon;
                                }
                            }
                        }

                        if (hitBattlePokemon != null) {
                            battle.sendUpdate(new BattleCaptureEndPacket(hitBattlePokemon.getPNX(), false));
                            BattleCaptureAction captureAction = null;
                            for(BattleCaptureAction action : battle.getCaptureActions()) {
                                if(action.getPokeBallEntity() == self) captureAction = action;
                            }
                            if(captureAction != null) {
                                battle.finishCaptureAction(captureAction);
                            }
                        }
                    }
                    if (showMessages) {
                        player.sendSystemMessage(Component.translatableWithFallback("catchlevelcap.fail_message","It seems is too strong to be caught... levelcap(%s)", playerLevel+levelCap)
                                .withStyle(style -> style.withColor(TextColor.fromRgb(0xFF0000))),true);
                    }
                    WorldExtensionsKt.playSoundServer(self.level(), self.position(), SoundEvents.ITEM_BREAK, SoundSource.NEUTRAL, 0.8F, 1F);
                    drop();
                    ci.cancel();
                }

//                if (!capturingPokemon.isBattling()) {
//                    displayLevel = playerLevel; // 非战斗时显示外部等级上限
//                    if(targetLevel > playerLevel) {
//                        if (showMessages) {
//                            if (externalLevelCap < levelCap && targetLevel < playerLevel + levelCap) {
//                                // 第一行：外部等级上限消息
//                                player.sendMessage(
//                                        Text.translatableWithFallback("catchlevelcap.fail_message_external","The ball bounced off... try battling first? levelcap(%s)", displayLevel).formatted(Formatting.RED),
//                                        true
//                                );
//                            } else {
//                                // 第一行：普通等级上限消息
//                                player.sendMessage(
//                                        Text.translatableWithFallback("catchlevelcap.fail_message","It seems is too strong to be caught... levelcap(%s)", displayLevel).formatted(Formatting.RED),
//                                        true
//                                );
//                            }
//                        }
//
//                        WorldExtensionsKt.playSoundServer(self.getWorld(), self.getPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.NEUTRAL, 0.8F, 1F);
//                        drop();
//                        ci.cancel();
//                    }
//                } else if (targetLevel > playerLevel + levelCap) {
//                    displayLevel = playerLevel + levelCap;
//
//                    PokemonBattle battle = PlayerExtensionsKt.getBattleState(player).component1();
//
//                    ActiveBattlePokemon hitBattlePokemon = null;
//                    if(battle != null) {
//                        for (ActiveBattlePokemon battlePokemon : battle.getActor(player).getSide().getOppositeSide().getActivePokemon()) {
//                            if (battlePokemon.getBattlePokemon().getEffectedPokemon().getEntity() == capturingPokemon) {
//                                hitBattlePokemon = battlePokemon;
//                            }
//                        }
//                    }
//                    if(hitBattlePokemon != null) {
//                        battle.broadcastChatMessage(Text.translatableWithFallback("catchlevelcap.fail_message_battle",
//                                "The ball had no effect on \n  (levelcap {1})...", displayLevel).formatted(Formatting.RED));
//                        battle.sendUpdate(new BattleCaptureEndPacket(hitBattlePokemon.getPNX(), false));
//
//                        BattleCaptureAction captureAction = null;
//                        for(BattleCaptureAction action : battle.getCaptureActions()) {
//                            if(action.getPokeBallEntity() == self) captureAction = action;
//                        }
//                        if(captureAction != null) {
//                            battle.finishCaptureAction(captureAction);
//                        }
//                    }
//
//                    if (showMessages) {
//                        player.sendMessage(Text.translatableWithFallback("catchlevelcap.fail_message",
//                                "It seems  is too strong to be caught... \n (levelcap {1})", targetLevel).formatted(Formatting.RED), true);
//                    }
//
//                    WorldExtensionsKt.playSoundServer(self.getWorld(), self.getPos(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.NEUTRAL, 0.8F, 1F);
//                    drop();
//                    ci.cancel();
//                }
            }
        }

    }

}
