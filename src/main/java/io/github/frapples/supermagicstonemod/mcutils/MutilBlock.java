package io.github.frapples.supermagicstonemod.mcutils;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * Created by minecraft on 17-3-1.
 */
public class MutilBlock {
    private MutilBlockSingleStruct[] structs;
    static public MutilBlock fireplace() {
        String[][][] s = {
                {
                        {"minecraft:brick_block", "minecraft:brick_block", "minecraft:brick_block"},
                        {"minecraft:brick_block", "minecraft:netherrack", "minecraft:brick_block"},
                        {"minecraft:brick_block", "minecraft:brick_block", "minecraft:brick_block"}
                },
                {
                        {"minecraft:brick_block", "minecraft:iron_bars", "minecraft:brick_block"},
                        {"minecraft:iron_bars", "minecraft:fire", "minecraft:iron_bars"},
                        {"minecraft:brick_block", "minecraft:iron_bars", "minecraft:brick_block"}

                },

                {
                        {"minecraft:brick_stairs", "minecraft:brick_stairs", "minecraft:brick_stairs"},
                        {"minecraft:brick_stairs", "minecraft:iron_bars", "minecraft:brick_stairs"},
                        {"minecraft:brick_stairs", "minecraft:brick_stairs", "minecraft:brick_stairs"}
                }
        };

        String [][][] s2 = {
                {
                        {"minecraft:stonebrick", "minecraft:stonebrick", "minecraft:stonebrick"},
                        {"minecraft:stonebrick", "minecraft:netherrack", "minecraft:stonebrick"},
                        {"minecraft:stonebrick", "minecraft:stonebrick", "minecraft:stonebrick"}
                },
                {
                        {"minecraft:stonebrick", "minecraft:iron_bars", "minecraft:stonebrick"},
                        {"minecraft:iron_bars", "minecraft:fire", "minecraft:iron_bars"},
                        {"minecraft:stonebrick", "minecraft:iron_bars", "minecraft:stonebrick"}

                },

                {
                        {"minecraft:stone_brick_stairs", "minecraft:stone_brick_stairs", "minecraft:stone_brick_stairs"},
                        {"minecraft:stone_brick_stairs", "minecraft:iron_bars", "minecraft:stone_brick_stairs"},
                        {"minecraft:stone_brick_stairs", "minecraft:stone_brick_stairs", "minecraft:stone_brick_stairs"}
                }

        };
        return new MutilBlock(s, s2);
    }

    public MutilBlock(String[][][] ... structs) {
        this.structs = new MutilBlockSingleStruct[structs.length];
        for (int i = 0; i < structs.length; i++) {
            this.structs[i] = new MutilBlockSingleStruct(structs[i]);
        }
    }



    static public class NotExistsException extends Exception{

    }
    public BlockPos locateCenterPosition(World worldIn, BlockPos pos) throws NotExistsException {
        for (MutilBlockSingleStruct struct : structs) {
            try {
                return struct.locateCenterPosition(worldIn, pos);
            } catch (MutilBlockSingleStruct.NotExistsException e) {
            }
        }
        throw new NotExistsException();
    }

    public Boolean isExists(World worldIn, BlockPos pos) {
        try {
            locateCenterPosition(worldIn, pos);
            return true;
        } catch (NotExistsException e) {
            return false;
        }
    }


}
