package com.ipixelmon.team;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.text.TextFormatting;

public enum EnumTeam {
    Colossus(TextFormatting.BLUE, EnumDyeColor.BLUE),
    Manta(TextFormatting.RED, EnumDyeColor.RED),
    Omicron(TextFormatting.YELLOW, EnumDyeColor.YELLOW),
    None(TextFormatting.DARK_GRAY, EnumDyeColor.WHITE);

    private final TextFormatting colorChat;
    private final EnumDyeColor colorDye;

    EnumTeam(TextFormatting colorChat, EnumDyeColor colorDye) {
        this.colorChat = colorChat;
        this.colorDye = colorDye;
    }

    public static EnumTeam getTeamFromID(int id) {
        return id == 0 ? Colossus : id == 1 ? Manta : id == 2 ? Omicron : None;
    }

    public TextFormatting colorChat() {
        return colorChat;
    }

    public EnumDyeColor colorDye() {
        return colorDye;
    }
}
