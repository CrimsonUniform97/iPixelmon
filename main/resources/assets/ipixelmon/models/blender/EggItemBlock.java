package assets.ipixelmon.models.blender;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.Random;

public class EggItemBlock extends ItemBlock
{
    public EggItemBlock(final Block block)
    {
        super(block);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            if (stack.getTagCompound().hasKey("pokeEggDistance"))
            {
                return EnumChatFormatting.GOLD + "" + stack.getTagCompound().getInteger("pokeEggDistance") + "Km Pokémon Egg";
            }
        }
        return EnumChatFormatting.GOLD + "Pokémon Egg";
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        if (stack.hasTagCompound())
        {
            tooltip.add("" + (int) stack.getTagCompound().getDouble("pokeEggWalked"));
        }
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
        nbtTag.setInteger("pokeEggDistance", distance);
        nbtTag.setDouble("pokeEggWalked", 0);
        stack.setTagCompound(nbtTag);
    }

    public boolean tick(EntityPlayer player, ItemStack stack)
    {
        if(!stack.hasTagCompound())
        {
            initTag(stack);
        }

        String[] data = player.getEntityData().getString("pokeEggLocation").split(",");
        BlockPos pos = new BlockPos(Double.parseDouble(data[0]), player.posY, Double.parseDouble(data[1]));

        double xOffset = Math.abs(pos.getX() - player.posX);
        double zOffset = Math.abs(pos.getZ() - player.posZ);
        if ((int) xOffset > 0 || (int) zOffset > 0)
        {
            stack.getTagCompound().setDouble("pokeEggWalked", stack.getTagCompound().getDouble("pokeEggWalked") + Math.sqrt(xOffset * xOffset + zOffset * zOffset));
            return true;
        }

        if(stack.getTagCompound().getDouble("pokeEggWalked") >= stack.getTagCompound().getInteger("pokeEggDistance"))
        {

        }

        return false;
    }



}
