package io.github.frapples.supermagicstonemod;

/**
 * Created by minecraft on 17-2-22.
 */

import io.github.frapples.supermagicstonemod.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ModMain.MODID, name = ModMain.NAME, version = ModMain.VERSION, acceptedMinecraftVersions = "1.8.9")
public class ModMain
{
    public static final String MODID = "supermagicstonemod";
    public static final String NAME = "SuperMagicStoneMod";
    public static final String VERSION = "0.0.1";

    @Instance(ModMain.MODID)
    public static ModMain instance;

    @SidedProxy(clientSide = "io.github.frapples.supermagicstonemod.client.ClientProxy",
            serverSide = "io.github.frapples.supermagicstonemod.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
