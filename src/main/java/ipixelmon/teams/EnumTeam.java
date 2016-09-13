package ipixelmon.teams;

import net.minecraft.util.EnumChatFormatting;

public enum EnumTeam
{
    Valor(EnumChatFormatting.RED), Mystic(EnumChatFormatting.BLUE), Instinct(EnumChatFormatting.YELLOW), None(EnumChatFormatting.DARK_GRAY);

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
