package io.github.frapples.supermagicstonemod.mcutils.ProcessBar;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by minecraft on 17-2-28.
 */
public class TimeProcessBar extends ProcessBar {

    private long time = 1;
    private long startMs = System.currentTimeMillis();

    public TimeProcessBar(EntityPlayer player) {
        super(player);
    }

    public TimeProcessBar setTime(long time) {
        this.time = time;
        return this;
    }

    @Override
    public double getPercent() {
        return (double)(System.currentTimeMillis() - startMs) / time;
    }

    @Override
    public void open() {
        startMs = System.currentTimeMillis();
        super.open();
    }
}
