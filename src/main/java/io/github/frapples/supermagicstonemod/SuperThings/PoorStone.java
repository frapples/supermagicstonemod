package io.github.frapples.supermagicstonemod.SuperThings;

import io.github.frapples.supermagicstonemod.mcutils.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;


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

        this.setMaxDamage(10);

        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public void onDone(EntityPlayer player, BindingPos pos, ItemStack stack) {
        BlockPos movePos = Utils.nearAirPosition(pos.world, pos.pos);
        player.setPositionAndUpdate(movePos.getX(), movePos.getY(), movePos.getZ());
        player.setFire(10);
        player.addPotionEffect(
                new PotionEffect(Potion.fireResistance.id, 300));
        stack.damageItem(5, player);
        player.addChatMessage(new ChatComponentText("" + stack.getItemDamage()));
    }


    @Override
    public Boolean beforeUsedHook(EntityPlayer playerIn, BindingPos pos) {
        try {
            if (Utils.getIdByWorld(pos.world) != Utils.getIdByWorld(playerIn.getEntityWorld())) {
                playerIn.addChatMessage(new ChatComponentTranslation(
                        "info_in_chat.not_same_world"));
                return false;
            } else {
                return true;
            }
        } catch (Utils.NotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double usingTime(EntityPlayer player, BindingPos pos) {
        return super.usingTime(player, pos) * 5;
    }
}


