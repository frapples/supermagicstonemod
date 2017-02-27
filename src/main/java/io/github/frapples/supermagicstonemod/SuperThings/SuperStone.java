package io.github.frapples.supermagicstonemod.SuperThings;

import com.sun.corba.se.spi.orbutil.fsm.StateImpl;
import io.github.frapples.supermagicstonemod.mcutils.MutilBlock;
import io.github.frapples.supermagicstonemod.mcutils.ProcessBar;
import io.github.frapples.supermagicstonemod.mcutils.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;

import javax.swing.text.Position;

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

        public BlockPos getPosition() {
            return new BlockPos(x, y, z);
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
        if (worldIn.isRemote) {
            return false;
        }


        BlockPos bindingPos;
        MutilBlock mutilFrieplace = MutilBlock.fireplace();
       if (mutilFrieplace.isExists(worldIn, blockPos)) {
           try {
               bindingPos = mutilFrieplace.locateCenterBlock(worldIn, blockPos);
           } catch (MutilBlock.BlockNotFound blockNotFound) {
               blockNotFound.printStackTrace();
               return false;
           }

       } else if (Block.getIdFromBlock(worldIn.getBlockState(blockPos).getBlock())
                == Block.getIdFromBlock(SuperFireplace.self())) {
           bindingPos = blockPos;
        } else {
            return false;
       }

        if (playerIn.isSneaking()) {
            SuperStone.bindTo(stack, worldIn, bindingPos);

            playerIn.addChatMessage(new ChatComponentTranslation(
                    "info_in_chat.binding_to", SuperStone.getBinded(stack).toString()));

            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    }


    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        if (!worldIn.isRemote)
        {
            if (playerIn.isSneaking())
            {
            } else {
                SuperStone.use(itemStackIn, worldIn, playerIn);
            }
        }

        return itemStackIn;
    }


    public static Boolean use(ItemStack stack, final World worldIn, final EntityPlayer playerIn)
    {
        // 为 true 的时候，世界正在运行在逻辑客户端内。如果这个值为 false，世界正在运行在逻辑服务器上
        if (worldIn.isRemote) {
            return false;
        }

        if (!stack.hasTagCompound()) {
            playerIn.addChatMessage(new ChatComponentTranslation("info_in_chat.not_binding"));
            return false;
        }

        final BindingPos pos = new BindingPos(stack.getTagCompound());

        if ((pos.getBlockState().getBlock() != SuperFireplace.self()) &&
        (!MutilBlock.fireplace().isExists(worldIn, new BlockPos(pos.x, pos.y, pos.z)))) {
            playerIn.addChatMessage(new ChatComponentTranslation(
                    "info_in_chat.super_fireplace_not_exists"));
            return false;
        }

        try {

            final long startMs = System.currentTimeMillis();
            final double s = usingTime(playerIn, pos);
            final BlockPos oldPos = playerIn.getPosition();
            if (pos.world == Utils.getIdByWorld(worldIn)) {


                ProcessBar.open(playerIn, new ProcessBar.ProcessBarInfoInterface() {

                    public double getPercent() {
                        return (double)(System.currentTimeMillis() - startMs) / (double)(s * 1000);
                    }

                    public void onProcessDone() {
                        playerIn.closeScreen();
                        BlockPos movePos = Utils.nearAirPosition(worldIn, pos.getPosition());
                        playerIn.setPositionAndUpdate(movePos.getX(), movePos.getY(), movePos.getZ());
                        playerIn.setFire(10);
                        playerIn.addPotionEffect(
                                new PotionEffect(Potion.fireResistance.id, 300));
                    }

                    public void onUpdated() {
                        if (!playerIn.getPosition().equals(oldPos)) {
                            playerIn.closeScreen();
                        }

                    }
                });

                worldIn.playSoundAtEntity(playerIn, "supermagicstonemod:test", 1.0F, 1.0F);


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

    // 施法引导时间
    static public double usingTime(EntityPlayer player, BindingPos pos) {
        double distance = Math.sqrt(player.getPosition().distanceSq(pos.x, pos.y, pos.z));

        final double factor = 0.1;
        final double minTime = 2;
        final double maxTime = 90;

        double time = Math.pow(distance, 2.0 / 3.0) * factor;

        time = time < minTime ? minTime : time;
        time = time > maxTime ? maxTime : time;
        return time;
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


