package com.ipixelmon.landcontrol.toolCupboard;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.client.PlayerListener;
import com.ipixelmon.landcontrol.packet.PacketOpenGui;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colby on 1/6/2017.
 */
public class ToolCupboardBlock extends BlockContainer {

    public static final ToolCupboardBlock instance = new ToolCupboardBlock();
    public static final String name = "toolCupboard";

    public static net.minecraft.block.properties.PropertyInteger MODEL;
    public static PropertyDirection FACING;

    private ToolCupboardBlock() {
        super(Material.rock);
        setUnlocalizedName(iPixelmon.id + ":" + name);
        setRegistryName(name);
        setDefaultState(blockState.getBaseState().withProperty(MODEL, 0).withProperty(FACING, EnumFacing.NORTH));
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher();
        renderItem.getItemModelMesher().register(
                Item.getItemFromBlock(this),
                0,
                new ModelResourceLocation(iPixelmon.id + ":toolCupboard", "inventory"));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        /**
         * flag 2 sends the update to the client immediately.
         */

        EnumFacing enumFacing = placer.getHorizontalFacing().getOpposite();

        worldIn.setBlockState(pos, getDefaultState().withProperty(MODEL, 0));
        worldIn.setBlockState(pos.up(), getDefaultState().withProperty(MODEL, 1));

        ToolCupboardTileEntity tileEntityBottom = (ToolCupboardTileEntity) worldIn.getTileEntity(pos);
        tileEntityBottom.setFacing(enumFacing);

        ToolCupboardTileEntity tileEntityTop = (ToolCupboardTileEntity) worldIn.getTileEntity(pos.up());
        tileEntityTop.setFacing(enumFacing);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return worldIn.isAirBlock(pos.up()) && LandControlAPI.Server.getTileEntity(worldIn, pos) == null;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) {
            if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof ToolCupboardTileEntity) {
                ToolCupboardTileEntity tileEntity = (ToolCupboardTileEntity) worldIn.getTileEntity(pos);
                if (tileEntity.getBaseTile() != null && playerIn.isSneaking()) {
                    if (PlayerListener.selectedTile != null && PlayerListener.selectedTile == tileEntity.getBaseTile()) {
                        PlayerListener.selectedTile = null;
                    } else {
                        PlayerListener.selectedTile = tileEntity.getBaseTile();
                    }
                }
            } else {
                PlayerListener.selectedTile = null;
            }
            return true;
        }

        return true;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(worldIn.getBlockState(pos.up()).getBlock() == instance) {
            worldIn.setBlockToAir(pos.up());
        } else if (worldIn.getBlockState(pos.down()).getBlock() == instance) {
            worldIn.setBlockToAir(pos.down());
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        ToolCupboardTileEntity cupboardTileEntity = (ToolCupboardTileEntity) worldIn.getTileEntity(pos);
        return getDefaultState().withProperty(MODEL, state.getValue(MODEL)).withProperty(FACING, cupboardTileEntity.getFacing());
    }

    @Override
    protected BlockState createBlockState() {
        MODEL = PropertyInteger.create("meta", 0, 1);
        FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        return new BlockState(this, new IProperty[]{MODEL, FACING});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(MODEL, meta == 0 ? 0 : 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if(state.getBlock() == this) {
            return state.getValue(MODEL);
        }

        return 0;
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
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ToolCupboardTileEntity();
    }
}
