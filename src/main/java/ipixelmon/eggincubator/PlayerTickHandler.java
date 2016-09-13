package ipixelmon.eggincubator;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerTickHandler
{

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(!event.player.getEntityData().hasKey("pokeEggLocation"))
        {
            event.player.getEntityData().setString("pokeEggLocation", event.player.posX + "," + event.player.posZ);
        }

        boolean moved = false;
        for(ItemStack stack : event.player.inventory.mainInventory)
        {
            if(stack != null)
            {
                if(stack.getItem() == EggIncubator.itemPokeEgg)
                {
                    if(((ItemPokeEgg) stack.getItem()).tick(event.player, stack))
                    {
                        moved = true;
                    }
                }
            }
        }

        if(moved)
        {
            event.player.getEntityData().setString("pokeEggLocation", event.player.posX + "," + event.player.posZ);
        }
    }

}
