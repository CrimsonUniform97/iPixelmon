package ipixelmon;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Optional;

public final class PixelmonUtility {

    @SideOnly(Side.SERVER)
    public static final void giveMoney(final EntityPlayerMP player, final int balance) {
        try {
            final PlayerStorage targetStorage = PixelmonStorage.PokeballManager.getPlayerStorage(player);
            targetStorage.addCurrency(Math.abs(balance));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.SERVER)
    public static final void takeMoney(final EntityPlayerMP player, final int balance) {
        try {
            final PlayerStorage targetStorage = PixelmonStorage.PokeballManager.getPlayerStorage(player);
            targetStorage.addCurrency(-1 * Math.abs(balance));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static final ItemStack makePokemonItem(final EnumPokemon pokemon) {
        final ItemStack stack = new ItemStack(PixelmonItems.itemPixelmonSprite);
        final NBTTagCompound tagCompound = new NBTTagCompound();
        final Optional stats = Entity3HasStats.getBaseStats(pokemon.name);
        tagCompound.setString("SpriteName", "pixelmon:sprites/pokemon/" + String.format("%03d", new Object[]{Integer.valueOf(((BaseStats)stats.get()).nationalPokedexNumber)}));
        final NBTTagCompound display = new NBTTagCompound();
        display.setString("Name", EntityPixelmon.getLocalizedName(pokemon.name) + " " + StatCollector.translateToLocal("item.PixelmonSprite.name"));
        tagCompound.setTag("display", display);
        stack.setTagCompound(tagCompound);
        return stack;
    }

    public static final int getPokemonCountClient() {
        int count = 0;
        for(int i = 0; i < ServerStorageDisplay.pokemon.length; ++i) {
            if(ServerStorageDisplay.pokemon[i] != null) count++;
        }

        return count;
    }

}
