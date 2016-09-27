package ipixelmon.teams;

import net.minecraft.util.EnumChatFormatting;

public enum EnumTeam
{
    Colossus(EnumChatFormatting.BLUE), Manta(EnumChatFormatting.RED), Omicron(EnumChatFormatting.YELLOW), None(EnumChatFormatting.DARK_GRAY);

    private final EnumChatFormatting color;

    EnumTeam(EnumChatFormatting color)
    {
        this.color = color;
    }

    public static EnumTeam getTeamFromID(int id)
    {
        return id == 0 ? Colossus : id == 1 ? Manta : id == 2 ? Omicron : None;
    }

    public EnumChatFormatting color()
    {
        return color;
    }
}
