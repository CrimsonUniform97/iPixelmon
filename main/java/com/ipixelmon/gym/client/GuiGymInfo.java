package com.ipixelmon.gym.client;

import com.google.common.collect.Maps;
import com.ipixelmon.gym.EntityTrainer;
import com.ipixelmon.gym.Gym;
import com.ipixelmon.gym.packet.PacketBattle;
import com.ipixelmon.gym.packet.PacketJoinGym;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.util.GuiUtil;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuiGymInfo extends GuiScreen {

    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(iPixelmon.id, "textures/gui/landcontrol/RegionBG.png");
    private static final int BG_HEIGHT = 234, BG_WIDTH = 256;
    private int POS_X, POS_Y;
    private int page = 0;
    private GuiPickPixelmon guiPickPixelmon;
    private Map<UUID, PixelmonAPI.Client.PixelmonRenderer> renderers = Maps.newHashMap();

    private Gym gym;

    public GuiGymInfo(Gym gym) {
        this.gym = gym;

        PixelmonAPI.Client.PixelmonRenderer pixelmonRenderer;
        for (UUID id : gym.getTrainers().keySet()) {

            EntityPixelmon entityPixelmon = gym.getTrainers().get(id);

            EntityTrainer entityTrainer = new EntityTrainer(Minecraft.getMinecraft().theWorld,
                    Minecraft.getMinecraft().thePlayer.getPosition(), id, entityPixelmon);

            gym.getTrainerEntities().add(entityTrainer);

            renderers.put(entityTrainer.getPlayerID(), pixelmonRenderer = PixelmonAPI.Client.renderPixelmon3D(entityPixelmon, true, this));
            new Thread(pixelmonRenderer).start();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1, 1, 1, 1);

        if (guiPickPixelmon.enabled) {
            guiPickPixelmon.drawScreen(mc, mouseX, mouseY);
            return;
        }

        /**
         * Draw background
         */
        mc.getTextureManager().bindTexture(BG_TEXTURE);
        drawTexturedModalRect(POS_X, POS_Y, 0, 0, BG_WIDTH, BG_HEIGHT);

        /**
         * Draw team name
         */
        String teamTxt = gym.getTeam().colorChat().toString() + EnumChatFormatting.BOLD.toString() + gym.getTeam().name();
        mc.fontRendererObj.drawString(teamTxt, (this.width - fontRendererObj.getStringWidth(teamTxt)) / 2, POS_Y + 10,
                0xFFFFFF, true);

        /**
         * Draw prestige stats
         */
        int level = gym.getLevel() + 1;
        level = level > 9 ? 9 : level;
        String prestigeTxt = (gym.getPrestige() / 1000D) + "K/" + (Gym.LEVELS[level] / 1000) + "K Prestige";
        fontRendererObj.drawString(EnumChatFormatting.YELLOW + prestigeTxt,
                POS_X + BG_WIDTH - fontRendererObj.getStringWidth(prestigeTxt) - 5, POS_Y + 5, 0xFFFFFF, true);

        if (!gym.getTrainerEntities().isEmpty()) {
            /**
             * Draw player
             */
            int x = (this.width / 2) - 30;
            int y = POS_Y + BG_HEIGHT - 90;

            EntityTrainer entityTrainer = (EntityTrainer) gym.getTrainerEntities().toArray()[page];

            GuiUtil.drawEntityOnScreen(x, y, 50, -(mouseX - x), -(mouseY - y) - 40, entityTrainer);

            /**
             * Draw pokemon
             */
            PixelmonAPI.Client.PixelmonRenderer pixelmonRenderer = renderers.get(entityTrainer.getPlayerID());
            pixelmonRenderer.render((this.width / 2) + 30, POS_Y + BG_HEIGHT - 40, 30);
            PixelmonAPI.Client.renderPixelmonTip(entityTrainer.getPixelmon(), POS_X, POS_Y + 24, this.width, this.height);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        guiPickPixelmon.mouseClicked(mc, mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        switch (button.id) {
            case 0: {
                page = page - 1 < 0 ? 0 : page - 1;
                break;
            }
            case 1: {
                page = page + 1 > gym.getTrainerEntities().size() ? gym.getTrainerEntities().size() : page + 1;
                break;
            }
            case 2: {
                guiPickPixelmon.enabled = true;
                break;
            }
            case 3: {
                Minecraft.getMinecraft().displayGuiScreen(null);
                iPixelmon.network.sendToServer(new PacketBattle(gym));
                break;
            }
            case 4: {
                guiPickPixelmon.enabled = true;
                break;
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        POS_X = (this.width - BG_WIDTH) / 2;
        POS_Y = (this.height - BG_HEIGHT) / 2;

        this.buttonList.add(new GuiButton(0, POS_X + 5, POS_Y + ((this.height - 20) / 2),
                20, 20, "<"));
        this.buttonList.add(new GuiButton(1, POS_X + BG_WIDTH - 25, POS_Y + ((this.height - 20) / 2),
                20, 20, ">"));

        this.buttonList.add(new MCButton(2, (this.width - 55) / 2, (this.height - 20) / 2, 55,
                "Claim Gym!", 4));

        this.buttonList.add(new GuiButton(3, POS_X + ((BG_WIDTH - 50) / 2), POS_Y + BG_HEIGHT - 25,
                50, 20, EnumChatFormatting.BOLD + "BATTLE"));
        this.buttonList.add(new GuiButton(4, POS_X + (BG_WIDTH - 45), POS_Y + BG_HEIGHT - 25,
                40, 20, EnumChatFormatting.BOLD + "JOIN!"));

        this.buttonList.get(0).enabled = false;
        this.buttonList.get(1).enabled = false;
        this.buttonList.get(4).enabled = false;

        this.buttonList.get(0).visible = !gym.getTrainerEntities().isEmpty();
        this.buttonList.get(1).visible = !gym.getTrainerEntities().isEmpty();
        this.buttonList.get(2).visible = gym.getTrainerEntities().isEmpty();
        this.buttonList.get(3).visible = !gym.getTrainerEntities().isEmpty();
        this.buttonList.get(4).visible = !gym.getTrainerEntities().isEmpty();

        guiPickPixelmon = new GuiPickPixelmon(this) {
            @Override
            public void onSelect(EntityPixelmon pixelmonData) {
                iPixelmon.network.sendToServer(new PacketJoinGym(gym.getID(), pixelmonData.getPokemonId()));
            }
        };

        guiPickPixelmon.initGui();
        guiPickPixelmon.enabled = false;
    }

    @Override
    public void updateScreen() {
        guiPickPixelmon.updateScreen();
        this.buttonList.get(0).enabled = !(page <= 0);
        this.buttonList.get(1).enabled = !(page >= gym.getTrainers().size() - 1);
        this.buttonList.get(4).enabled = gym.getAvailableSeats() > 0 && !gym.getTrainers().containsKey(mc.thePlayer.getUniqueID());
    }

    private class MCButton extends GuiButton {

        private int scale;
        private Rectangle bounds;

        public MCButton(int buttonId, int x, int y, int width, String buttonText, int scale) {
            super(buttonId, x, y, buttonText);
            this.width = width;
            this.height = 20;
            this.scale = scale;

            int posX = x + (width / 2);
            int posXMouse = posX - ((width * scale) / 2);

            int posY = y + (height / 2);
            int posYMouse = posY - ((height * scale) / 2);

            bounds = new Rectangle(posXMouse, posYMouse, width * scale, height * scale);
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(xPosition, yPosition, 0);
                GlStateManager.translate(width / 2, height / 2, 0);
                GlStateManager.scale(scale, scale, scale);
                GlStateManager.translate(-(width / 2), -(height / 2), 0);
                {

                    FontRenderer fontrenderer = mc.fontRendererObj;
                    mc.getTextureManager().bindTexture(buttonTextures);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.hovered = bounds.contains(mouseX, mouseY);
                    int i = this.getHoverState(this.hovered);
                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    GlStateManager.blendFunc(770, 771);
                    this.drawTexturedModalRect(0, 0, 0, 46 + i * 20, this.width / 2, this.height);
                    this.drawTexturedModalRect(0 + this.width / 2, 0, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
                    this.mouseDragged(mc, mouseX, mouseY);
                    int j = 14737632;

                    if (packedFGColour != 0) {
                        j = packedFGColour;
                    } else if (!this.enabled) {
                        j = 10526880;
                    } else if (this.hovered) {
                        j = 16777120;
                    }

                    this.drawCenteredString(fontrenderer, this.displayString, 0 + this.width / 2, 0 + (this.height - 8) / 2, j);
                }

                GlStateManager.popMatrix();
            }
        }

        @Override
        public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
            if (!this.enabled || !this.visible) return false;
            return bounds.contains(mouseX, mouseY);
        }
    }
}
