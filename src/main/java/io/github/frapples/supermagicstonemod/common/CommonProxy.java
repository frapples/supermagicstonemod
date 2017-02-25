package io.github.frapples.supermagicstonemod.common;

import io.github.frapples.supermagicstonemod.SuperThings.SuperFireplace;
import io.github.frapples.supermagicstonemod.SuperThings.SuperStone;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by minecraft on 17-2-22.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event)
    {

        GameRegistry.registerItem(SuperStone.self(), SuperStone.ID);

        SuperFireplace block = SuperFireplace.self();
        GameRegistry.registerBlock(block, SuperFireplace.ID);
        new ItemBlock(block).setRegistryName(block.getRegistryName());

    }

    public void init(FMLInitializationEvent event)
    {

    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }
}
