package com.ipixelmon;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class HiddenBlock extends Block {

    public static final HiddenBlock instance = new HiddenBlock();

    private HiddenBlock() {
        super(Material.barrier);
        setRegistryName("hiddenBlock");
        setUnlocalizedName("hiddenBlock");
        setCreativeTab(CreativeTabs.tabBlock);
        setHardness(Float.MAX_VALUE);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

}
