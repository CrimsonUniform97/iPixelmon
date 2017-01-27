package com.ipixelmon.itemdisplay;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.permission.PermissionAPI;
import com.ipixelmon.realestate.client.ForSaleSignTileEntity;
import com.ipixelmon.realestate.client.RenderForSaleSignTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class ItemDisplayBlock extends BlockContainer {

    public static final ItemDisplayBlock instance = new ItemDisplayBlock();
    public static PropertyDirection facing;

    private ItemDisplayBlock() {
        super(Material.IRON);
        setRegistryName("displayBlock");
        setUnlocalizedName("displayBlock");
        setCreativeTab(CreativeTabs.DECORATIONS);
        setDefaultState(blockState.getBaseState().withProperty(facing, EnumFacing.NORTH));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        facing = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
        return new BlockStateContainer(this, new IProperty[]{facing});
    }

    // TODO: Add item icon, look into the RealEstate mod and check out the block for adding item model

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(facing, placer.getHorizontalFacing().getOpposite()), 2);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return true;

        if(worldIn.getTileEntity(pos) == null) return false;
        if(!(worldIn.getTileEntity(pos) instanceof ItemDisplayBlockTileEntity)) return false;

        if(!PermissionAPI.hasPermission(playerIn.getUniqueID(), "itemdisplay.interact")) return false;

        if(playerIn.isSneaking()) {
            iPixelmon.network.sendTo(new PacketOpenGui(pos), (EntityPlayerMP) playerIn);
            return true;
        }

        ItemDisplayBlockTileEntity te = (ItemDisplayBlockTileEntity) worldIn.getTileEntity(pos);

        if(heldItem == null) return false;

        te.setItemStack(heldItem);

        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(facing, enumfacing);
    }


    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(facing)).getIndex();
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ItemDisplayBlockTileEntity();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

    @Override
    public boolean isBlockNormalCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isCollidable() {
        return false;
    }

    @Override
    public BlockStateContainer getBlockState() {
        return new BlockStateContainer(this, new IProperty[] {facing});
    }
}
