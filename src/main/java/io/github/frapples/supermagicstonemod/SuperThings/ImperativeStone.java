package io.github.frapples.supermagicstonemod.SuperThings;

import io.github.frapples.supermagicstonemod.mcutils.Utils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

/**
 * Created by minecraft on 17-3-1.
 */
public class ImperativeStone extends SuperStone{

    static public final String ID = "imperative_stone";

    static private ImperativeStone instance;
    static public ImperativeStone self()
    {
        if (instance == null) {
            instance = new ImperativeStone();
        }
        return instance;
    }


    protected ImperativeStone()
    {
        super();
        this.setMaxStackSize(1);
        this.setUnlocalizedName(ID);

        this.setMaxDamage(100);

        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public void onDone(EntityPlayer player, BindingPos pos, ItemStack stack) {
        if (player.getEntityWorld().provider.getDimensionId() != pos.world.provider.getDimensionId()) {
            player.travelToDimension(pos.world.provider.getDimensionId());
        }

        BlockPos movePos = Utils.nearAirPosition(pos.world, pos.pos);
        player.setPositionAndUpdate(movePos.getX(), movePos.getY(), movePos.getZ());
        player.setFire(10);
        player.addPotionEffect(
                new PotionEffect(Potion.fireResistance.id, 300));
        stack.damageItem(1, player);
    }


    @Override
    public Boolean beforeUsedHook(EntityPlayer playerIn, BindingPos pos) {
        return true;
    }

    @Override
    public double usingTime(EntityPlayer player, BindingPos pos) {
        return 0.01;
    }
}
