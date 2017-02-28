package io.github.frapples.supermagicstonemod.mcutils.ProcessBar;

import io.github.frapples.supermagicstonemod.init.ModMain;
import io.github.frapples.supermagicstonemod.init.GuiLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

/**
 * Created by minecraft on 17-2-26.
 */
public class ProcessBar {
    protected EntityPlayer player;
    public ProcessBar(EntityPlayer player) {
        this.player = player;
    }


    private String sound = "";
    private long soundTime = 0;
    private float soundVolume;
    public ProcessBar setSound(String sound, long soundTime, float volume) {
        this.sound = sound;
        this.soundTime = soundTime;
        this.soundVolume = volume;
        return this;
    }

    public void open() {
        final ProcessBar self = this;
        Implementation.ProcessBarImplementationContainer.info = new Implementation.ProcessBarInfoInterface() {
            public double getPercent() {
                return self.getPercent();
            }

            public void onProcessDone() {
                self.onProcessDone();

            }

            private long lastPlaySound = System.currentTimeMillis() - soundTime;
            public void onUpdated() {
                if (soundTime > 0 && System.currentTimeMillis() - lastPlaySound > soundTime) {
                    player.getEntityWorld().playSoundAtEntity(player, sound, soundVolume, 1.0F);
                    lastPlaySound = System.currentTimeMillis();
                }
                self.onUpdated();
            }
        };

        BlockPos pos = player.getPosition();
        player.openGui(ModMain.instance,
                GuiLoader.PROCESS_BAR_ID,
                player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ());
    }


    public double getPercent() {
        return 0;
    }

    public void onProcessDone() {
    }

    public void onUpdated() {
    }
}


