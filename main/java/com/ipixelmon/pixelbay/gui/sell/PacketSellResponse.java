package com.ipixelmon.pixelbay.gui.sell;

import com.pixelmonmod.pixelmon.comm.PixelmonData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSellResponse implements IMessage
{

    public PacketSellResponse()
    {
    }

    private ItemStack itemStack;
    private PixelmonData pixelmonData;

    public PacketSellResponse(ItemStack itemStack)
    {
        this.itemStack = itemStack;
    }

    public PacketSellResponse(PixelmonData pixelmonData)
    {
        this.pixelmonData = pixelmonData;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {

        boolean isItem = buf.readBoolean();

        if(isItem)
        {
            itemStack = ByteBufUtils.readItemStack(buf);
        } else
        {
            pixelmonData = new PixelmonData();
            pixelmonData.decodeInto(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        if(itemStack != null)
        {
            buf.writeBoolean(true);
            ByteBufUtils.writeItemStack(buf, itemStack);
        } else
        {
            buf.writeBoolean(false);
            pixelmonData.encodeInto(buf);
        }
    }

    public static class Handler implements IMessageHandler<PacketSellResponse, IMessage>
    {

        @Override
        public IMessage onMessage(PacketSellResponse message, MessageContext ctx)
        {
            handle(message);
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    private static void handle(final PacketSellResponse message)
    {
        Minecraft.getMinecraft().addScheduledTask(new Runnable()
        {
            @Override
            public void run()
            {
                if(Minecraft.getMinecraft().currentScreen != null)
                {
                    if(Minecraft.getMinecraft().currentScreen instanceof GuiSell)
                    {
                        GuiSell gui = (GuiSell) Minecraft.getMinecraft().currentScreen;

                        if(gui.scrollList instanceof ListItem)
                        {
                            ListItem itemList = (ListItem) gui.scrollList;
                            int i = 0;
                            for(ItemStack stack : itemList.items)
                            {
                                if(ItemStack.areItemStacksEqual(message.itemStack, stack))
                                {
                                    itemList.items.remove(i);
                                    break;
                                }
                                i++;
                            }
                        } else
                        {
                            ListPokemon pokeList = (ListPokemon) gui.scrollList;
                            pokeList.pokemon.remove(message.pixelmonData);
                        }
                    }
                }
            }
        });
    }
}
