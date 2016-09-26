package ipixelmon.teams;

import net.minecraft.util.EnumChatFormatting;

public enum EnumTeam
{
    Manta(EnumChatFormatting.RED), Omicron(EnumChatFormatting.YELLOW), Colossus(EnumChatFormatting.BLUE), None(EnumChatFormatting.DARK_GRAY);

    private final EnumChatFormatting color;

    EnumTeam(EnumChatFormatting color)
    {
        this.color = color;
    }

    public EnumChatFormatting getColor()
    {
        return color;
    }
}
