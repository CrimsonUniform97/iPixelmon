package ipixelmon.eggincubator;

import ipixelmon.eggincubator.egg.EggItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerTickHandler
{

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
//        if(!event.player.getEntityData().hasKey("pokeEggLocation"))
//        {
//            event.player.getEntityData().setString("pokeEggLocation", event.player.posX + "," + event.player.posZ);
//        }
//
//        boolean moved = false;
//        for(ItemStack stack : event.player.inventory.mainInventory)
//        {
//            if(stack != null)
//            {
//                if(stack.getItem() == Item.getItemFromBlock(EggBlock.instance))
//                {
//                    if(((EggItem) stack.getItem()).tick(event.player, stack))
//                    {
//                        moved = true;
//                    }
//                }
//            }
//        }
//
//        if(moved)
//        {
//            event.player.getEntityData().setString("pokeEggLocation", event.player.posX + "," + event.player.posZ);
//        }
    }

}
