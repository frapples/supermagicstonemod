package io.github.frapples.supermagicstonemod.SuperThings;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by minecraft on 17-3-1.
 */
public class Config
{
    static public double superStoneWaitingTimeMultiplier = 0;
    static public int poorStoneMaxDamage = 0;
    static public int imperativeStoneMaxDamage  = 0;

    static public double poorStoneWaitingTimeMultiplierToSuperStone = 0;

    static public void init(FMLPreInitializationEvent event)
    {
        Configuration config = new Configuration(event.getSuggestedConfigurationFile());

        config.load();

        load(config);
        config.save();
    }

    static private void load(Configuration config) {
        superStoneWaitingTimeMultiplier = config.get(Configuration.CATEGORY_GENERAL,
                "superStoneMultiplier",
                0.1,
                "pow(distance, 2.0 / 3.0) * mul").getDouble();


        poorStoneWaitingTimeMultiplierToSuperStone = config.get(Configuration.CATEGORY_GENERAL,
                "poorStoneWaitingTimeMultiplierToSuperStone ",
                3.8,
                "pow(distance, 2.0 / 3.0) * mul").getDouble();

        poorStoneMaxDamage = config.get(Configuration.CATEGORY_GENERAL,
                "poorStoneMaxDamage",
                5,
                "Poor Stone max Damage").getInt();


        imperativeStoneMaxDamage = config.get(Configuration.CATEGORY_GENERAL,
                "imperativeStoneMaxDamage",
                3,
                "Imperative Stone max damage").getInt();


    }

}
