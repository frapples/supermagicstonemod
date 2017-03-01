package io.github.frapples.supermagicstonemod.SuperThings;

import io.github.frapples.supermagicstonemod.mcutils.CanUsedItem;
import io.github.frapples.supermagicstonemod.mcutils.MutilBlock;
import io.github.frapples.supermagicstonemod.mcutils.ProcessBar.TimeProcessBar;
import io.github.frapples.supermagicstonemod.mcutils.Utils;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;


/**
 * Created by minecraft on 17-2-22.
 */
public class PoorStone extends SuperStone {
    static public final String ID = "poor_stone";

    static private PoorStone instance;
    static public PoorStone self()
    {
        if (instance == null) {
            instance = new PoorStone();
        }
        return instance;
    }


    protected PoorStone()
    {
        super();
        this.setMaxStackSize(1);
        this.setUnlocalizedName(ID);

        this.setCreativeTab(CreativeTabs.tabTools);
    }







}


