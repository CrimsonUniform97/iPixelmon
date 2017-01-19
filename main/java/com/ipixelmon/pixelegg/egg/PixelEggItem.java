package com.ipixelmon.pixelegg.egg;

import com.ipixelmon.pixelegg.PacketOpenGuiPixelEgg;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.achievement.PixelmonAchievements;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.ipixelmon.iPixelmon;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class PixelEggItem extends Item
{

    public static final PixelEggItem instance = new PixelEggItem();

    private PixelEggItem()
    {
        setUnlocalizedName("pixelegg");
        setRegistryName("pixelegg");
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        final ModelResourceLocation normalModel = new ModelResourceLocation(iPixelmon.id + ":pixelegg", "inventory");
        final ModelResourceLocation crackedModel = new ModelResourceLocation(iPixelmon.id + ":pixelegg_crack1", "inventory");
        final ModelResourceLocation cracked2Model = new ModelResourceLocation(iPixelmon.id + ":pixelegg_crack2", "inventory");

        ModelBakery.registerItemVariants(this, normalModel, crackedModel, cracked2Model);

        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                if (stack.getMetadata() == 0) {
                    return normalModel;
                } else if (stack.getMetadata() == 1) {
                    return crackedModel;
                } else {
                    return cracked2Model;
                }
            }
        });
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            if (stack.getTagCompound().hasKey("pixelEggDistance"))
            {
                return TextFormatting.GOLD + "" + stack.getTagCompound().getInteger("pixelEggDistance") + "Km PixelEgg";
            }
        } else {
            initTag(stack);
        }
        return TextFormatting.GOLD + "PixelEgg";
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        if (stack.hasTagCompound())
        {
            tooltip.add("" + (int) stack.getTagCompound().getDouble("pixelEggWalked"));
        } else {
            initTag(stack);
        }
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, final float hitX, final float hitY, final float hitZ)
    {
        if(worldIn.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }

        if(stack.getTagCompound().getDouble("pixelEggWalked") >= stack.getTagCompound().getInteger("pixelEggDistance") * 1000)
        {
            EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(EggHatchingList.instance.getRandomPokemon(stack.getTagCompound().getInteger("pixelEggDistance")).name, worldIn);
            pokemon.setHealth(pokemon.getMaxHealth());
            pokemon.getLvl().setLevel(MathHelper.getRandomIntegerInRange(new Random(), 50, 99));
            pokemon.caughtBall = EnumPokeballs.PokeBall;
            pokemon.friendship.initFromCapture();
            try
            {
                PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP) playerIn).get().addToParty(pokemon);
            } catch (Exception e)
            {
                e.printStackTrace();
                playerIn.addChatComponentMessage(new TextComponentString("An error occurred. Tell an admin."));
                return EnumActionResult.FAIL;
            }
            PixelmonAchievements.pokedexChieves((EntityPlayerMP) playerIn);
            Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent((EntityPlayerMP) playerIn, ReceiveType.Command, pokemon));
            iPixelmon.network.sendTo(new PacketOpenGuiPixelEgg(new PixelmonData(pokemon)), (EntityPlayerMP) playerIn);
            playerIn.inventory.removeStackFromSlot(playerIn.inventory.currentItem);
            ((EntityPlayerMP) playerIn).updateHeldItem();
        }
        return EnumActionResult.SUCCESS;
    }

    private void initTag(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            return;
        }

        NBTTagCompound nbtTag = new NBTTagCompound();
        int distance = 2;
        int rarity = new Random().nextInt(100);
        if (rarity <= 10)
        {
            distance = 10;
        } else if (rarity <= 40)
        {
            distance = 5;
        }
        nbtTag.setInteger("pixelEggDistance", distance);
        nbtTag.setDouble("pixelEggWalked", 0);
        stack.setTagCompound(nbtTag);
    }

    public boolean tick(EntityPlayer player, ItemStack stack)
    {
        if(!stack.hasTagCompound())
        {
            initTag(stack);
        }

        String[] data = player.getEntityData().getString("pixelEggLocation").split(",");
        BlockPos pos = new BlockPos(Double.parseDouble(data[0]), player.posY, Double.parseDouble(data[1]));

        double xOffset = Math.abs(pos.getX() - player.posX);
        double zOffset = Math.abs(pos.getZ() - player.posZ);
        if ((int) xOffset > 0 || (int) zOffset > 0)
        {
            if(stack.getTagCompound().getDouble("pixelEggWalked") >= stack.getTagCompound().getInteger("pixelEggDistance") * 1000)
            {
                return true;
            }
            stack.getTagCompound().setDouble("pixelEggWalked", stack.getTagCompound().getDouble("pixelEggWalked") + Math.sqrt(xOffset * xOffset + zOffset * zOffset));
            return true;
        }

        return false;
    }

}
