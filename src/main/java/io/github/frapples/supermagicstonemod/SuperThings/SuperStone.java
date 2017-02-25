package io.github.frapples.supermagicstonemod.SuperThings;

import io.github.frapples.supermagicstonemod.mcutils.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * Created by minecraft on 17-2-22.
 */
public class SuperStone extends Item {
    static public final String ID = "super_stone";

    static private SuperStone instance;
    static public SuperStone self()
    {
        if (instance == null) {
            instance = new SuperStone();
        }
        return instance;
    }

    static class BindingPos {
        int world;
        int x;
        int y;
        int z;

        public BindingPos(NBTTagCompound data) {
            world = data.getInteger("world");
            x = data.getInteger("X");
            y = data.getInteger("Y");
            z = data.getInteger("Z");

        }

        public BindingPos(int idByWorld, int x, int y, int z) {
            world = idByWorld;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public NBTTagCompound toNBT() {
            NBTTagCompound data = new NBTTagCompound();
            data.setInteger("world", world);
            data.setInteger("X",  x);
            data.setInteger("Y",  y);
            data.setInteger("Z",  z);
            return data;

        }

        @Override
        public String toString() {
            return String.format("[%d:(%d, %d, %d)]", world, x, y, z);
        }

        public IBlockState getBlockState() {
            return Utils.getWorldById(world).getBlockState(new BlockPos(x, y, z));
        }

    }
    protected SuperStone()
    {
        super();
        this.setMaxStackSize(1);
        this.setUnlocalizedName(ID);

        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn,
                             BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        // 为 true 的时候，世界正在运行在逻辑客户端内。如果这个值为 false，世界正在运行在逻辑服务器上
        if (worldIn.isRemote) {
            return false;
        } else {
            if (!stack.hasTagCompound()) {
                // playerIn.addChatMessage(new ChatComponentText("Not bind"));
                playerIn.addChatMessage(new ChatComponentTranslation("info_in_chat.not_binding"));
                return false;
            }

            BindingPos pos = new BindingPos(stack.getTagCompound());
            playerIn.addChatMessage(new ChatComponentTranslation(
                    "info_in_chat.display_binded", pos.toString()));

            if (pos.getBlockState().getBlock() != SuperFireplace.self()) {
                playerIn.addChatMessage(new ChatComponentTranslation(
                        "info_in_chat.super_fireplace_not_exists"));
                return false;
            }

            try {
                if (pos.world == Utils.getIdByWorld(worldIn)) {
                    playerIn.setPositionAndUpdate(pos.x, pos.y + 1, pos.z);
                    return true;
                } else {
                    playerIn.addChatMessage(new ChatComponentTranslation(
                            "info_in_chat.not_same_world"));
                    return false;
                }
            } catch (Utils.NotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    public ItemStack newItemStack(World world, int x, int y, int z)
    {
        NBTTagCompound data;
        try {
            data = (new BindingPos(Utils.getIdByWorld(world), x, y, z)).toNBT();

        } catch (Utils.NotFoundException e) {
            data = null;
        }

        ItemStack s = new ItemStack(SuperStone.self(), 1);
        s.setTagCompound(data);

        return s;
    }
}


