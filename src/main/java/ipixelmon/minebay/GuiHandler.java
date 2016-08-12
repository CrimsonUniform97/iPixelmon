package ipixelmon.minebay;

import ipixelmon.minebay.gui.search.SearchGui;
import ipixelmon.minebay.gui.sell.SellGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public final class GuiHandler implements IGuiHandler {
    @Override
    public final Object getServerGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        return null;
    }

    @Override
    public final Object getClientGuiElement(final int ID, final EntityPlayer player, final World world, final int x, final int y, final int z) {
        return ID == SearchGui.ID ? new SearchGui() : ID == SellGui.ID ? new SellGui() : null;
    }
}
