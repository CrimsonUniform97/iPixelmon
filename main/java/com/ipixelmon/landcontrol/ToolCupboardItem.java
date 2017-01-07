package com.ipixelmon.landcontrol;

import com.ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colby on 1/6/2017.
 */
public class ToolCupboardItem extends Item {

    public static final ToolCupboardItem instance = new ToolCupboardItem();

    private ToolCupboardItem() {
        setCreativeTab(CreativeTabs.tabBlock);
        setUnlocalizedName(iPixelmon.id + ":toolCupboardItem");
        setRegistryName("toolCupboardItem");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
                .register(this, 0,
                        new ModelResourceLocation(iPixelmon.id + ":toolCupboardItem", "inventory"));
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return true;

        if(!ToolCupboardBlock.instance.canPlaceBlockAt(worldIn, pos.offset(side))) return false;

        ToolCupboardBlock.instance.onBlockPlacedBy(worldIn, pos.offset(side), ToolCupboardBlock.instance.getDefaultState(), playerIn, stack);

        return true;
    }
}
