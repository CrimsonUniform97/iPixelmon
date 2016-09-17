package ipixelmon.eggincubator.egg;

import ipixelmon.iPixelmon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class EggBlock extends Block
{
    public static final String name = "egg";
    public static final EggBlock instance = new EggBlock();


    private EggBlock()
    {
        super(Material.iron);
        setCreativeTab(CreativeTabs.tabBlock);
        setUnlocalizedName(iPixelmon.id + ":" + name);
        setRegistryName(name);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean isFullCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return 3;
    }

}