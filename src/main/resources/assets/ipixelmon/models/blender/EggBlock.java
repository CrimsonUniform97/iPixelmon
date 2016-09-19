package assets.ipixelmon.models.blender;

import ipixelmon.iPixelmon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

public class EggBlock extends Block
{
    public static final String name = "egg";
    public static final EggBlock instance = new EggBlock();
    public static PropertyInteger EGG_STAGE;

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

    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn)
    {
        System.out.println("CALLED");
        worldIn.setBlockState(pos, worldIn.getBlockState(pos) .withProperty(EGG_STAGE, 1));
    }

    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ)
    {
        worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(EGG_STAGE, 1));
        return true;
    }

    @Override
    public BlockState createBlockState()
    {
        if(EGG_STAGE == null)
        {
            EGG_STAGE = PropertyInteger.create("eggstage", 0, 2);
        }
        return new ExtendedBlockState(this, new IProperty[]{EGG_STAGE}, new IUnlistedProperty[]{OBJModel.OBJProperty.instance});
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(EGG_STAGE, meta > 2 ? 2 : meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(EGG_STAGE);
    }
}