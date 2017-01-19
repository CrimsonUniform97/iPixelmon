package com.ipixelmon;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class HiddenBlock extends Block {

    public static final HiddenBlock instance = new HiddenBlock();

    private HiddenBlock() {
        super(Material.BARRIER);
        setRegistryName("hiddenBlock");
        setUnlocalizedName("hiddenBlock");
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setHardness(Float.MAX_VALUE);
    }



    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

}
