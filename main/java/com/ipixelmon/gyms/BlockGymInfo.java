package com.ipixelmon.gyms;

import com.ipixelmon.gyms.client.TileEntityGymInfo;
import com.ipixelmon.iPixelmon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by colby on 10/16/2016.
 */
public class BlockGymInfo extends BlockContainer {

    public static final BlockGymInfo instance = new BlockGymInfo();

    private BlockGymInfo() {
        super(Material.iron);
        setCreativeTab(CreativeTabs.tabBlock);
        setRegistryName(new ResourceLocation(iPixelmon.id, "gym_info"));
        setUnlocalizedName("gym_info");
    }
// TODO: Get rendering to work.
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityGymInfo();
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
}
