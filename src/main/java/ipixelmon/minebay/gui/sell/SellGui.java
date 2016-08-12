package ipixelmon.minebay.gui.sell;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public final class SellGui extends GuiScreen {

    public static final int ID = 987;

    @Override
    public final void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected final void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected final void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected final void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public final void initGui() {
        super.initGui();

        try {

            PixelmonData[] poke = ServerStorageDisplay.pokemon;
            System.out.println(poke.length);
            for (PixelmonData data : poke) {
                if (data != null)
                    System.out.println(data.name);
            }

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void updateScreen() {
        super.updateScreen();
    }
}
