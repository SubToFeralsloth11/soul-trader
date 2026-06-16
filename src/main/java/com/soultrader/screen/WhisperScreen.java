package com.soultrader.screen;

import com.soultrader.SoulTraderMod;
import com.soultrader.item.ModItems;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class WhisperScreen extends HandledScreen<WhisperScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(SoulTraderMod.MOD_ID, "textures/gui/whisper_trade.png");
    private ButtonWidget buyButton;
    private ButtonWidget sellButton;

    public WhisperScreen(WhisperScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.buyButton = this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.soultrader.buy_soul"),
                button -> {
                    this.client.interactionManager.clickButton(this.handler.syncId, 0);
                    this.buyButton.active = false;
                    if (!this.sellButton.active) this.close();
                }
        ).dimensions(this.x + 20, this.y + 68, 60, 20).build());

        this.sellButton = this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.soultrader.sell_soul"),
                button -> {
                    this.client.interactionManager.clickButton(this.handler.syncId, 1);
                    this.sellButton.active = false;
                    if (!this.buyButton.active) this.close();
                }
        ).dimensions(this.x + 96, this.y + 68, 60, 20).build());
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);

        boolean buyUsed = !this.buyButton.active;
        boolean sellUsed = !this.sellButton.active;
        ItemStack costStack = handler.getBuyOfferCost();
        ItemStack rewardStack = handler.getSellReward();
        var itemRenderer = this.client.getItemRenderer();

        context.drawText(this.textRenderer, Text.translatable("entity.soultrader.whisper"), this.x + 50, this.y + 6, 0xFFD700, true);

        context.drawText(this.textRenderer, Text.translatable("gui.soultrader.buy_soul"), this.x + 24, this.y + 30, 0xAAAAAA, true);
        if (!buyUsed) {
            itemRenderer.renderInGui(costStack, this.x + 40, this.y + 48);
            context.drawText(this.textRenderer, "->", this.x + 62, this.y + 52, 0xFFFFFF, true);
            itemRenderer.renderInGui(new ItemStack(ModItems.SOUL), this.x + 82, this.y + 48);
        } else {
            context.drawText(this.textRenderer, Text.translatable("gui.soultrader.sold_out"), this.x + 24, this.y + 48, 0xFF5555, true);
        }

        context.drawText(this.textRenderer, Text.translatable("gui.soultrader.sell_soul"), this.x + 112, this.y + 30, 0xAAAAAA, true);
        if (!sellUsed) {
            itemRenderer.renderInGui(new ItemStack(ModItems.SOUL), this.x + 98, this.y + 48);
            context.drawText(this.textRenderer, "->", this.x + 120, this.y + 52, 0xFFFFFF, true);
            itemRenderer.renderInGui(rewardStack, this.x + 140, this.y + 48);
        } else {
            context.drawText(this.textRenderer, Text.translatable("gui.soultrader.sold_out"), this.x + 108, this.y + 48, 0xFF5555, true);
        }
    }
}
