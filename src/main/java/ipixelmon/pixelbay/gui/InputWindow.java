package ipixelmon.pixelbay.gui;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;

public abstract class InputWindow extends Gui
{

    private static final ResourceLocation bgTexture = new ResourceLocation(iPixelmon.id + ":pixelbay/textures/gui/PopupWindow.png");
    public static final int width = 176, height = 88;

    protected final int xPosition, yPosition;
    public GuiTextField textField;
    protected GuiButton actionBtn;
    public boolean visible;

    public InputWindow(FontRenderer fontRenderer, int xPosition, int yPosition, String btnTxt)
    {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        int section = this.height / 2;
        this.textField = new GuiTextField(0, fontRenderer, this.xPosition + ((this.width - (this.width - 50)) / 2), this.yPosition + ((section * 0) + ((section - 20) / 2)), this.width - 50, 20);
        textField.setFocused(true);
        textField.setCanLoseFocus(false);
        this.actionBtn = new GuiButton(1, this.xPosition + ((this.width - (this.width - 100)) / 2), this.yPosition + ((section * 1) + ((section - 20) / 2)), this.width - 100, 20, btnTxt);
        this.visible = false;
    }

    public void draw(Minecraft mc, int mouseX, int mouseY)
    {
        if (!this.visible)
        {
            return;
        }

        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(bgTexture);
        drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);
        this.textField.drawTextBox();
        this.actionBtn.drawButton(mc, mouseX, mouseY);

        if ((Mouse.isButtonDown(0) && actionBtn.mousePressed(mc, mouseX, mouseY)))
        {
            actionPerformed();
        }
    }

    public void keyTyped(char typedChar, int keyCode)
    {
        if(!this.visible)
        {
            return;
        }

        if (keyCode == Keyboard.KEY_ESCAPE)
        {
            this.visible = false;
        } else if (keyCode == Keyboard.KEY_RETURN)
        {
            actionPerformed();
            this.visible = false;
        }

        textField.textboxKeyTyped(typedChar, keyCode);
    }



    public void update()
    {
        this.textField.updateCursorCounter();
    }

    public abstract void actionPerformed();
}
