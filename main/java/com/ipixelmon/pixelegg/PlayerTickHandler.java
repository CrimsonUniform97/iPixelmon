package com.ipixelmon.pixelegg;

import com.ipixelmon.pixelegg.egg.PixelEggItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class PlayerTickHandler
{

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if(!event.player.getEntityData().hasKey("pixelEggLocation"))
        {
            event.player.getEntityData().setString("pixelEggLocation", event.player.posX + "," + event.player.posZ);
        }

        boolean moved = false;
        for(ItemStack stack : event.player.inventory.mainInventory)
        {
            if(stack != null)
            {
                if(stack.getItem() == PixelEggItem.instance)
                {
                    if(PixelEggItem.instance.tick(event.player, stack))
                    {
                        moved = true;
                    }
                }
            }
        }

        if(moved)
        {
            event.player.getEntityData().setString("pixelEggLocation", event.player.posX + "," + event.player.posZ);
        }
    }

}
