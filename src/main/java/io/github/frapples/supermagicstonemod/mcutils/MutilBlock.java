package io.github.frapples.supermagicstonemod.mcutils;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;


/**
 * Created by minecraft on 17-2-26.
 */
public class MutilBlock {
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
        return new MutilBlock(s);
    }


    private String[][][] struct;

    public MutilBlock(String[][][] struct) {
        this.struct = struct;
    }

    public boolean isExists(World worldIn, BlockPos pos) {
        try {
            BlockPos center = locateCenterBlock(worldIn, pos);
            BlockPos numMinPoint = new BlockPos(
                    center.getX() - getOffsetX() / 2,
                    center.getY() - getOffsetY() / 2,
                    center.getZ() - getOffsetZ() / 2);


            for (int x = 0; x < getOffsetX(); x++)
                for (int y = 0; y < getOffsetY(); y++)
                    for (int z = 0; z < getOffsetZ(); z++) {

                        if (!getBlockName(x, y, z).equals(
                                worldIn.getBlockState(numMinPoint.add(x, y, z)).getBlock().getRegistryName())) {
                            return false;
                        }

                    }

            return true;

        } catch (BlockNotFound blockNotFound) {
            return false;
        }
    }


    public static class BlockNotFound extends Exception {

    }
    public BlockPos locateCenterBlock(World world, BlockPos pos) throws BlockNotFound {
        for (int x = pos.getX() - getOffsetX() / 2; x <= pos.getX() + getOffsetX() / 2; x++)
            for (int y = pos.getY() - getOffsetY() / 2; y <= pos.getY() + getOffsetY() / 2; y++)
                for (int z = pos.getZ() - getOffsetZ() / 2; z <= pos.getZ() + getOffsetZ() / 2; z++) {

                    if (CenterBlockName().equals(
                            world.getBlockState(new BlockPos(x, y, z)).getBlock().getRegistryName())){
                        return new BlockPos(x, y, z);
                    }

                }
        throw new BlockNotFound();
    }

    public  String getBlockName(int x, int y, int z) {
        // 竖 宽 长
        return struct[y][z][x];
    }
    public String CenterBlockName() {
        return getBlockName(getOffsetX() / 2, getOffsetY() / 2, getOffsetZ() / 2);
    }

    public int getOffsetX() {
        return struct[0][0].length;
    }

    public int getOffsetY() {
        return struct[0].length;
    }

    public int getOffsetZ() {
        return struct.length;
    }
}
