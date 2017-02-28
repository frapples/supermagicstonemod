package io.github.frapples.supermagicstonemod.SuperThings;

import io.github.frapples.supermagicstonemod.mcutils.CanUsedItem;
import io.github.frapples.supermagicstonemod.mcutils.MutilBlock;
import io.github.frapples.supermagicstonemod.mcutils.ProcessBar.Implementation;
import io.github.frapples.supermagicstonemod.mcutils.ProcessBar.ProcessBar;
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
import net.minecraft.util.*;
import net.minecraft.world.World;


/**
 * Created by minecraft on 17-2-22.
 */
public class SuperStone extends CanUsedItem {
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
        World world;
        BlockPos pos;

        public BindingPos(World world, BlockPos pos) {
            this.world = world;
            this.pos = pos;
        }

        public BindingPos(NBTTagCompound data) {
            this(Utils.getWorldById(data.getInteger("world")),
                    new BlockPos(
                            data.getInteger("X"),
                            data.getInteger("Y"),
                            data.getInteger("Z")));
        }


        public void writeToNBT(NBTTagCompound data) throws Utils.NotFoundException {
            data.setInteger("world", Utils.getIdByWorld(world));
            data.setInteger("X",  pos.getX());
            data.setInteger("Y",  pos.getY());
            data.setInteger("Z",  pos.getZ());
        }

        @Override
        public String toString() {
            try {
                return String.format("[%d:(%d, %d, %d)]", Utils.getIdByWorld(world),
                        pos.getX(),
                        pos.getY(),
                        pos.getZ());
            } catch (Utils.NotFoundException e) {
                e.printStackTrace();
                return "";
            }
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
    public boolean onShiftRightClickBlock(ItemStack stack, final EntityPlayer playerIn, World worldIn,
                             BlockPos blockPos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote) {
            return false;
        }

        BindingPos bindingPos = null;
        try {
            bindingPos = BindingBlocksBindingPoint(new BindingPos(worldIn, blockPos));
        } catch (BindingBlockNotExistsException e) {
            return false;
        }

            setBindingPos(stack, bindingPos.world, bindingPos.pos);
            playerIn.addChatMessage(new ChatComponentTranslation( "info_in_chat.binding_success"));
            return true;
    }


    @Override
    public ItemStack onRightClickAny(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (!worldIn.isRemote) {
            use(itemStackIn, worldIn, playerIn);
        }

        return itemStackIn;
    }


    @Override
    public ItemStack onShiftRightClickAny(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (worldIn.isRemote) {
            return itemStackIn;
        }

        try {
            playerIn.addChatMessage(new ChatComponentTranslation(
                    "info_in_chat.display_binded", getBindingPos(itemStackIn).toString()));
        } catch (NotBindingException e) {
            playerIn.addChatMessage(new ChatComponentTranslation(
                    "info_in_chat.not_binding"));
        }

        playerIn.addChatMessage(new ChatComponentTranslation("info_in_chat.super_stone_tip"));
        return itemStackIn;
    }

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        playerIn.addChatMessage(new ChatComponentTranslation("info_in_chat.super_stone_help"));
    }

    public Boolean use(ItemStack stack, final World worldIn, final EntityPlayer playerIn)
    {
        // 为 true 的时候，世界正在运行在逻辑客户端内。如果这个值为 false，世界正在运行在逻辑服务器上
        if (worldIn.isRemote) {
            return false;
        }

        BindingPos pos = null;
        try {
            pos = getBindingPos(stack);
        } catch (NotBindingException e) {
            playerIn.addChatMessage(new ChatComponentTranslation("info_in_chat.not_binding"));
            return false;
        }

        if (isBindingPosDamaged(pos)) {
            playerIn.addChatMessage(new ChatComponentTranslation(
                    "info_in_chat.super_fireplace_not_exists"));
            return false;
        }

        try {
            if (Utils.getIdByWorld(pos.world) != Utils.getIdByWorld(worldIn)) {
                playerIn.addChatMessage(new ChatComponentTranslation(
                        "info_in_chat.not_same_world"));
                return false;
            }
        } catch (Utils.NotFoundException e) {
            e.printStackTrace();
            return false;
        }

        final BlockPos oldPos = playerIn.getPosition();
        final BindingPos pos_ = pos;

        (new TimeProcessBar(playerIn) {
            public void onUpdated() {
                if (!playerIn.getPosition().equals(oldPos)) {
                    playerIn.closeScreen();
                }
            }

            public void onProcessDone() {
                playerIn.closeScreen();
                BlockPos movePos = Utils.nearAirPosition(worldIn, pos_.pos);
                playerIn.setPositionAndUpdate(movePos.getX(), movePos.getY(), movePos.getZ());
                playerIn.setFire(10);
                playerIn.addPotionEffect(
                        new PotionEffect(Potion.fireResistance.id, 300));
            }

        }).setTime((long)usingTime(playerIn, pos) * 1000)
                .setSound("supermagicstonemod:test", 2 * 1000, 3.5F)
                .open();

        return true;
    }

    public boolean isBindingPosDamaged(BindingPos pos) {
        Block block = pos.world.getBlockState(pos.pos).getBlock();
        return (block != SuperFireplace.self() && (!MutilBlock.fireplace().isExists(pos.world, pos.pos)));
    }


    static class BindingBlockNotExistsException extends Exception {

    }
    /* 传入点击的欲绑定点，返回绑定的位置 */
    public BindingPos BindingBlocksBindingPoint(BindingPos pos) throws BindingBlockNotExistsException {
        Block block = pos.world.getBlockState(pos.pos).getBlock();

        if (block == SuperFireplace.self()) {
            return pos;
        }

        if (MutilBlock.fireplace().isExists(pos.world, pos.pos)) {
            try {
                return new BindingPos(
                        pos.world,
                        MutilBlock.fireplace().locateCenterBlock(pos.world, pos.pos));
            } catch (MutilBlock.BlockNotFound blockNotFound) {
                blockNotFound.printStackTrace();
            }
        }
        throw new BindingBlockNotExistsException();
    }

    // 施法引导时间
    static public double usingTime(EntityPlayer player, BindingPos pos) {
        double distance = Math.sqrt(
                player.getPosition().distanceSq(pos.pos.getX(), pos.pos.getY(), pos.pos.getZ()));

        final double factor = 0.1;
        final double minTime = 2;
        final double maxTime = 90;

        double time = Math.pow(distance, 2.0 / 3.0) * factor;

        time = time < minTime ? minTime : time;
        time = time > maxTime ? maxTime : time;
        return time;
    }


    public void setBindingPos(ItemStack stack, World worldIn, BlockPos pos) {
        Item item = stack.getItem();
        if (Item.getIdFromItem(item) == Item.getIdFromItem(SuperStone.self())) {

            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }

            try {
                (new BindingPos(worldIn, pos)).writeToNBT(stack.getTagCompound());
            } catch (Utils.NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public BindingPos getBindingPos(ItemStack stack) throws NotBindingException {
        if (!stack.hasTagCompound()) {
            throw new NotBindingException();
        }
        return new SuperStone.BindingPos(stack.getTagCompound());
    }

    public void setLabel(ItemStack stack, String label) {
        stack.setStackDisplayName(String.format(
                "%s(%s)",
                Utils.getItemTranslateName(stack.getItem()),
                label ));
    }

    static class NotBindingException extends Utils.NotFoundException {

    }

}


