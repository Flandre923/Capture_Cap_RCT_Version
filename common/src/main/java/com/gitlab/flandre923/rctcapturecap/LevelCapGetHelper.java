package com.gitlab.flandre923.rctcapturecap;

import com.gitlab.srcmc.rctmod.api.RCTMod;
import com.gitlab.srcmc.rctmod.api.data.save.TrainerPlayerData;
import net.minecraft.server.level.ServerPlayer;

public class LevelCapGetHelper {
    public static int getLevelCap(ServerPlayer serverPlayer)
    {
        TrainerPlayerData data = RCTMod.getInstance().getTrainerManager().getData(serverPlayer);
        if(data!=null){
            int levelCap = data.getLevelCap();
            return levelCap;
        }
        return 0;
    }

}
