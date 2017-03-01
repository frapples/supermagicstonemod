package io.github.frapples.supermagicstonemod.mcutils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by minecraft on 17-2-28.
 */

/* 为了让损伤度生效，只能先继承ItemBow */
public class CanUsedItem extends ItemBow {
    public CanUsedItem() {
        super();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn,
                             BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            return this.onShiftRightClickBlock(stack, playerIn, worldIn, blockPos, side, hitX, hitY, hitZ);
        } else {
            return this.onRightClickBlock(stack, playerIn, worldIn, blockPos, side, hitX, hitY, hitZ);
        }
    }


    public boolean onShiftRightClickBlock(ItemStack stack, EntityPlayer playerIn, World worldIn,
                             BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }


    public boolean onRightClickBlock(ItemStack stack, EntityPlayer playerIn, World worldIn,
                                     BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }


    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (playerIn.isSneaking()) {
            return this.onShiftRightClickAny(itemStackIn, worldIn, playerIn);
        } else {
            return this.onRightClickAny(itemStackIn, worldIn, playerIn);
        }
    }

    public ItemStack onRightClickAny(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        return itemStackIn;

    }

    public ItemStack onShiftRightClickAny(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        return itemStackIn;
    }


    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    }
}
