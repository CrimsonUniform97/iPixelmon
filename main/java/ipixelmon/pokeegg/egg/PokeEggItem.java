package ipixelmon.pokeegg.egg;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.achievement.PixelmonAchievements;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonRecievedEvent;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import ipixelmon.iPixelmon;
import ipixelmon.landcontrol.PacketOpenRegionInfo;
import ipixelmon.pokeegg.PacketOpenGuiPokeEgg;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class PokeEggItem extends Item
{

    public static final PokeEggItem instance = new PokeEggItem();

    private PokeEggItem()
    {
        setUnlocalizedName("pokeegg");
        setRegistryName("pokeegg");
        setCreativeTab(CreativeTabs.tabBlock);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        final ModelResourceLocation normalModel = new ModelResourceLocation(getRegistryName(), "inventory");
        final ModelResourceLocation crackedModel = new ModelResourceLocation(getRegistryName() + "_crack1", "inventory");
        final ModelResourceLocation cracked2Model = new ModelResourceLocation(getRegistryName() + "_crack2", "inventory");

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
            if (stack.getTagCompound().hasKey("pokeEggDistance"))
            {
                return EnumChatFormatting.GOLD + "" + stack.getTagCompound().getInteger("pokeEggDistance") + "Km PokéEgg";
            }
        } else {
            initTag(stack);
        }
        return EnumChatFormatting.GOLD + "PokéEgg";
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        if (stack.hasTagCompound())
        {
            tooltip.add("" + (int) stack.getTagCompound().getDouble("pokeEggWalked"));
        } else {
            initTag(stack);
        }
    }

    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ)
    {
        if(worldIn.isRemote)
        {
            return false;
        }

        if(stack.getTagCompound().getDouble("pokeEggWalked") >= stack.getTagCompound().getInteger("pokeEggDistance") * 1000)
        {
            EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(EggHatchingList.instance.getRandomPokemon(stack.getTagCompound().getInteger("pokeEggDistance")).name, worldIn);
            pokemon.setHealth(pokemon.getMaxHealth());
            pokemon.getLvl().setLevel(MathHelper.getRandomIntegerInRange(new Random(), 50, 99));
            pokemon.caughtBall = EnumPokeballs.PokeBall;
            pokemon.friendship.initFromCapture();
            try
            {
                PixelmonStorage.PokeballManager.getPlayerStorage((EntityPlayerMP) playerIn).addToParty(pokemon);
            } catch (PlayerNotLoadedException e)
            {
                e.printStackTrace();
                playerIn.addChatComponentMessage(new ChatComponentText("An error occurred. Tell an admin."));
                return false;
            }
            PixelmonAchievements.pokedexChieves(playerIn);
            Pixelmon.EVENT_BUS.post(new PixelmonRecievedEvent(playerIn, ReceiveType.Command, pokemon));
            iPixelmon.network.sendTo(new PacketOpenGuiPokeEgg(new PixelmonData(pokemon)), (EntityPlayerMP) playerIn);
            playerIn.inventory.removeStackFromSlot(playerIn.inventory.currentItem);
            ((EntityPlayerMP) playerIn).updateHeldItem();
        }
        return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
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
            if(stack.getTagCompound().getDouble("pokeEggWalked") >= stack.getTagCompound().getInteger("pokeEggDistance") * 1000)
            {
                System.out.println("CALLED");
                return true;
            }
            stack.getTagCompound().setDouble("pokeEggWalked", stack.getTagCompound().getDouble("pokeEggWalked") + Math.sqrt(xOffset * xOffset + zOffset * zOffset));
            return true;
        }

        return false;
    }

}
