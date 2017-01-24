package com.ipixelmon.realestate.client;

import com.ipixelmon.iPixelmon;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ForSaleSignStanding extends BlockContainer {

    public ForSaleSignStanding() {
        super(Material.WOOD);
        setRegistryName("forSaleStandingSign");
        setUnlocalizedName("forSaleStandingSign");
        setDefaultState(blockState.getBaseState());
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        GameRegistry.register(this);
        GameRegistry.register(new ItemBlock(this), getRegistryName());
        GameRegistry.registerTileEntity(ForSaleSignTileEntity.class, "forSaleTileEntity");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        // TODO: Add SOLD model
        ModelResourceLocation normalModel = new ModelResourceLocation(
                iPixelmon.id + ":forSaleStandingSign", "inventory");
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(this), 0, normalModel);
        ClientRegistry.bindTileEntitySpecialRenderer(ForSaleSignTileEntity.class, new RenderForSaleSignTileEntity());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        int yaw = (int) placer.rotationYaw;
        if (yaw < 0) yaw += 360;
        yaw += 22;
        yaw %= 360;
        int facing = yaw / 45;
        EnumDirection direction = EnumDirection.values()[facing];
        ForSaleSignTileEntity tileEntityBottom = (ForSaleSignTileEntity) worldIn.getTileEntity(pos);
        tileEntityBottom.setDirection(direction);
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
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new ForSaleSignTileEntity();
    }

}
