package io.github.frapples.supermagicstonemod.init;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

/**
 * Created by minecraft on 17-3-1.
 */
public class EventLoader {


    public EventLoader() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerDrops(PlayerDropsEvent event)
    {
        System.out.println("test");
        EntityPlayer player = event.entityPlayer;
        InventoryPlayer inventory = event.entityPlayer.inventory;

        if (inventory.hasItem(Item.getItemFromBlock(Blocks.dirt))) {
            event.setCanceled(true);


            for (ItemStack item : inventory.mainInventory) {
                if (item.getItem().getRegistryName().equals("minecraft:dirt")) {
                    System.out.println(item.getItem().getRegistryName());
                } else {
                    player.getEntityWorld().spawnEntityInWorld(
                            new EntityItem(player.getEntityWorld(),
                                    player.getPosition().getX(),
                                    player.getPosition().getY(),
                                    player.getPosition().getZ(),
                                    item));
                }
            }

            for (ItemStack item : inventory.mainInventory) {
                event.entityPlayer.getEntityWorld().spawnEntityInWorld(
                        new EntityItem(player.getEntityWorld(),
                                player.getPosition().getX(),
                                player.getPosition().getY(),
                                player.getPosition().getZ(),
                                item));
            }

            inventory.clear();
        }
    }

}
