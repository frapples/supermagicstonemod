package io.github.frapples.supermagicstonemod.SuperThings;

import io.github.frapples.supermagicstonemod.mcutils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by minecraft on 17-2-22.
 */
public class SuperFireplace extends Block{
    static public final String ID = "super_fireplace";

    static private SuperFireplace instance = null;
    static public SuperFireplace self() {
        if (instance == null) {
            instance = new SuperFireplace();
        }
        return instance;
    }

    protected SuperFireplace()
    {
        super(Material.iron);
        this.setUnlocalizedName(ID);
        this.setHardness(2.5F);
        this.setStepSound(soundTypeMetal);

        this.setCreativeTab(CreativeTabs.tabBlock);

    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
                                    EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote) {
            InventoryPlayer inventory = playerIn.inventory;
            ItemStack stack = inventory.mainInventory[inventory.currentItem];

            SuperStone.bindTo(stack, worldIn, pos);
            playerIn.addChatMessage( new ChatComponentTranslation(
                    "info_in_chat.binding_to", SuperStone.getBinded(stack).toString()));
            return true;

        } else {
            return false;
        }
    }
}
