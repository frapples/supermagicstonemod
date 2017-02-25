package io.github.frapples.supermagicstonemod.mcutils;

import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

/**
 * Created by minecraft on 17-2-22.
 */
public class Utils {

    public static class NotFoundException extends Exception {

    }

    /* 注意：下面两个函数只针对服务器端有效 */
    public static int getIdByWorld(World world) throws NotFoundException {

        Integer[] ids = DimensionManager.getIDs();
        for (Integer id: ids) {
            World w = DimensionManager.getWorld(id.intValue());
            if (w == world) {
                return id;
            }
        }

        throw new NotFoundException();
    }

    public static WorldServer getWorldById(int id) {
        return DimensionManager.getWorld(id);
    }

    public static String getItemTranslateName(Item item) {

        // item.xxx.name
        String s = item.getUnlocalizedName() + ".name";
        return s == null ? "" : StatCollector.translateToLocal(s);
    }
}


