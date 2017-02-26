package io.github.frapples.supermagicstonemod.init;

import io.github.frapples.supermagicstonemod.mcutils.ProcessBar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by minecraft on 17-2-26.
 */
public class GuiLoader implements IGuiHandler
{
    public static final int PROCESS_BAR_ID = 1;


    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {


        switch (ID)
        {
            case PROCESS_BAR_ID:
                return new ProcessBar.ProcessBarImplementationContainer();
            default:
                return null;
        }
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        switch (ID)
        {
            case PROCESS_BAR_ID:
                return new ProcessBar.ProcessBarImplementationGuiContainer(
                        new ProcessBar.ProcessBarImplementationContainer());
            default:
                return null;
        }
    }
}
