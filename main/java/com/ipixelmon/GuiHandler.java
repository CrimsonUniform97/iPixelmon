package com.ipixelmon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public final class GuiHandler implements IGuiHandler {

    @Override
    public final Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        for (IMod mod : iPixelmon.mods)
            if (mod.getGuiHandler() != null)
                if (mod.getGuiHandler().getServerGuiElement(ID, player, world, x, y, z) != null)
                    return mod.getGuiHandler().getServerGuiElement(ID, player, world, x, y, z);
        return null;
    }

    @Override
    public final Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        for (IMod mod : iPixelmon.mods)
            if (mod.getGuiHandler() != null)
                if (mod.getGuiHandler().getClientGuiElement(ID, player, world, x, y, z) != null)
                    return mod.getGuiHandler().getClientGuiElement(ID, player, world, x, y, z);

        return null;
    }
}