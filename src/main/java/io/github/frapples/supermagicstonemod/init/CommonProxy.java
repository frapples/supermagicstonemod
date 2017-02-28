package io.github.frapples.supermagicstonemod.init;

import io.github.frapples.supermagicstonemod.SuperThings.CommandSuperStone;
import io.github.frapples.supermagicstonemod.SuperThings.SuperAshes;
import io.github.frapples.supermagicstonemod.SuperThings.SuperFireplace;
import io.github.frapples.supermagicstonemod.SuperThings.SuperStone;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by minecraft on 17-2-26.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event)
    {

        GameRegistry.registerItem(SuperStone.self(), SuperStone.ID);
        GameRegistry.registerItem(SuperAshes.self(), SuperAshes.ID);

        SuperFireplace block = SuperFireplace.self();
        GameRegistry.registerBlock(block, SuperFireplace.ID);
        new ItemBlock(block).setRegistryName(block.getRegistryName());

        registerGuiHandler(new GuiLoader());

    }

    public void init(FMLInitializationEvent event)
    {

        GameRegistry.addShapedRecipe(new ItemStack(SuperAshes.self(), 12), new Object[] {
                        " # ",
                        "#*#",
                        "###",
                        '#', Items.ender_pearl, '*', Blocks.redstone_block
                });


        GameRegistry.addShapedRecipe(new ItemStack(SuperStone.self()), new Object[] {
                "###",
                "#*#",
                "###",
                '#', Items.ender_pearl, '*', Items.diamond
        });

    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }

    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandSuperStone());
    }


    public void registerGuiHandler(IGuiHandler guiHandler)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(ModMain.instance, guiHandler);
    }
}
