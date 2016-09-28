package com.ipixelmon.teams.client;

import com.ipixelmon.teams.EnumTeam;
import io.netty.buffer.ByteBuf;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.teams.EnumTeam;
import com.ipixelmon.teams.Teams;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketChooseTeam implements IMessage
{

    public PacketChooseTeam()
    {}

    private int teamID;

    public PacketChooseTeam(int teamID)
    {
        this.teamID = teamID;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        teamID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(teamID);
    }

    public static class Handler implements IMessageHandler<PacketChooseTeam, IMessage>
    {

        @Override
        public IMessage onMessage(PacketChooseTeam message, MessageContext ctx)
        {

            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if(Teams.getPlayerTeam(player.getUniqueID()) != EnumTeam.None)
            {
                player.addChatComponentMessage(new ChatComponentText("You have already chosen your team. You cannot be reassigned."));
                return null;
            }

            if(EnumTeam.getTeamFromID(message.teamID) == EnumTeam.None)
            {
                player.addChatComponentMessage(new ChatComponentText("Invalid teamID."));
                return null;
            }

            InsertForm insertForm = new InsertForm("Players");
            insertForm.add("uuid", player.getUniqueID().toString());
            insertForm.add("team", EnumTeam.getTeamFromID(message.teamID).name());
            iPixelmon.mysql.insert(Teams.class, insertForm);
            ctx.getServerHandler().playerEntity.closeScreen();
            return null;
        }

    }

}
