package com.ipixelmon.landcontrol.toolCupboard;

import com.ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colby on 1/6/2017.
 */
public class ToolCupboardItem extends Item {

    public static final ToolCupboardItem instance = new ToolCupboardItem();

    private ToolCupboardItem() {
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
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
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) return EnumActionResult.SUCCESS;

        if (!ToolCupboardBlock.instance.canPlaceBlockAt(worldIn, pos.offset(facing))) return EnumActionResult.FAIL;

        ToolCupboardBlock.instance.onBlockPlacedBy(worldIn, pos.offset(facing), ToolCupboardBlock.instance.getDefaultState(), playerIn, stack);

        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
