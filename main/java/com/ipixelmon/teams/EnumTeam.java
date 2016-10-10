package com.ipixelmon.teams;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumChatFormatting;

public enum EnumTeam {
    Colossus(EnumChatFormatting.BLUE, EnumDyeColor.BLUE), Manta(EnumChatFormatting.RED, EnumDyeColor.RED), Omicron(EnumChatFormatting.YELLOW, EnumDyeColor.YELLOW), None(EnumChatFormatting.DARK_GRAY, EnumDyeColor.WHITE);

    private final EnumChatFormatting colorChat;
    private final EnumDyeColor colorDye;

    EnumTeam(EnumChatFormatting colorChat, EnumDyeColor colorDye) {
        this.colorChat = colorChat;
        this.colorDye = colorDye;
    }

    public static EnumTeam getTeamFromID(int id) {
        return id == 0 ? Colossus : id == 1 ? Manta : id == 2 ? Omicron : None;
    }

    public EnumChatFormatting colorChat() {
        return colorChat;
    }

    public EnumDyeColor colorDye() {
        return colorDye;
    }
}
