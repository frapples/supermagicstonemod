package io.github.frapples.supermagicstonemod.mcutils;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;


/**
 * Created by minecraft on 17-2-26.
 */
public class MutilBlockSingleStruct {


    private String[][][] struct;

    public MutilBlockSingleStruct(String[][][] struct) {
        this.struct = struct;
    }


    static public class NotExistsException extends Exception {

    }
    public BlockPos locateCenterPosition(World worldIn, BlockPos pos) throws NotExistsException {
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
                            throw new NotExistsException();
                        }
                    }

            return center;

        } catch (BlockNotFound blockNotFound) {
            throw new NotExistsException();
        }
    }


    public static class BlockNotFound extends Exception {

    }
    private BlockPos locateCenterBlock(World world, BlockPos pos) throws BlockNotFound {
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
