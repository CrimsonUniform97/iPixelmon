package ipixelmon.eggincubator.client;

import ipixelmon.eggincubator.client.GuiPokemonEggReward;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class TestListener
{

    @SubscribeEvent
    public void onbreak(BlockEvent.BreakEvent event)
    {
        Minecraft.getMinecraft().addScheduledTask(new Runnable()
        {
            @Override
            public void run()
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiPokemonEggReward());
            }
        });
    }

}
