package io.github.frapples.supermagicstonemod.SuperThings;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by minecraft on 17-2-28.
 */
public class SuperAshes extends Item {
    static public final String ID = "super_ashes";

    static private SuperAshes instance;
    static public SuperAshes self()
    {
        if (instance == null) {
            instance = new SuperAshes();
        }
        return instance;
    }


    protected SuperAshes()
    {
        super();
        this.setMaxStackSize(64);
        this.setUnlocalizedName(ID);

        this.setCreativeTab(CreativeTabs.tabTools);
    }

}
