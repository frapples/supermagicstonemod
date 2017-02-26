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

        public BindingPos(World world, BlockPos pos) throws Utils.NotFoundException {
            this(Utils.getIdByWorld(world), pos.getX(), pos.getY(), pos.getZ());
        }

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

        public void writeToNBT(NBTTagCompound data) {
            data.setInteger("world", world);
            data.setInteger("X",  x);
            data.setInteger("Y",  y);
            data.setInteger("Z",  z);
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
    public boolean onItemUse(ItemStack stack, final EntityPlayer playerIn, World worldIn,
                             BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        // 为 true 的时候，世界正在运行在逻辑客户端内。如果这个值为 false，世界正在运行在逻辑服务器上
        if (worldIn.isRemote) {
            return false;
        } else {
            if (!stack.hasTagCompound()) {
                playerIn.addChatMessage(new ChatComponentTranslation("info_in_chat.not_binding"));
                return false;
            }

            final BindingPos pos = new BindingPos(stack.getTagCompound());
            playerIn.addChatMessage(new ChatComponentTranslation(
                    "info_in_chat.display_binded", pos.toString()));

            if (pos.getBlockState().getBlock() != SuperFireplace.self()) {
                playerIn.addChatMessage(new ChatComponentTranslation(
                        "info_in_chat.super_fireplace_not_exists"));
                return false;
            }

            try {

                if (pos.world == Utils.getIdByWorld(worldIn)) {

                    (new Thread () {
                        public void run() {
                            try {
                                final double waitTime = 5;
                                final double stepTime = 0.5;

                                BlockPos oldPos = playerIn.getPosition();
                                for (double i = 0; i < waitTime; i += stepTime) {
                                    if (!oldPos.equals(playerIn.getPosition())) {
                                        playerIn.addChatMessage(new ChatComponentTranslation(
                                                "info_in_chat.canel_by_move"));
                                        return;
                                    }

                                    playerIn.addChatMessage(new ChatComponentTranslation(
                                            "info_in_chat.remain_time" , Double.toString(waitTime - i)));
                                    Thread.sleep((long)(i * 1000));
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            playerIn.setPositionAndUpdate(pos.x, pos.y + 1, pos.z);
                        }
                    }).start();

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

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    }



    static public void bindTo(ItemStack stack, World worldIn, BlockPos pos) {
        Item item = stack.getItem();
        if (Item.getIdFromItem(item) == Item.getIdFromItem(SuperStone.self())) {

            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }

            try {
                BindingPos bindingPos = new BindingPos(worldIn, pos);
                bindingPos.writeToNBT(stack.getTagCompound());
                stack.setStackDisplayName(
                        Utils.getItemTranslateName(stack.getItem()) + bindingPos.toString());
            } catch (Utils.NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    static public BindingPos getBinded(ItemStack stack) {
        return new SuperStone.BindingPos(stack.getTagCompound());
    }

}


