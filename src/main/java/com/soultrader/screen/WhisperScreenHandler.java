package com.soultrader.screen;

import com.soultrader.SoulTraderMod;
import com.soultrader.entity.WhisperEntity;
import com.soultrader.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;

public class WhisperScreenHandler extends ScreenHandler {
    private final int whisperEntityId;
    private WhisperEntity whisperServer;
    private WhisperData syncedData;
    private final PlayerInventory playerInventory;

    public WhisperScreenHandler(int syncId, PlayerInventory playerInventory, WhisperEntity whisper) {
        super(SoulTraderMod.WHISPER_SCREEN_HANDLER, syncId);
        this.whisperServer = whisper;
        this.whisperEntityId = whisper.getId();
        this.playerInventory = playerInventory;
        this.syncedData = new WhisperData(whisper.getId(), whisper.getCostType(), whisper.isBuyOfferUsed(), whisper.isSellOfferUsed(), whisper.getSellReward());
        addPlayerSlots();
    }

    public WhisperScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(SoulTraderMod.WHISPER_SCREEN_HANDLER, syncId);
        this.whisperServer = null;
        this.playerInventory = playerInventory;
        this.syncedData = WhisperData.PACKET_CODEC.decode((RegistryByteBuf) buf);
        this.whisperEntityId = syncedData.entityId();
        addPlayerSlots();
    }

    private void addPlayerSlots() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public WhisperData getSyncedData() {
        return syncedData;
    }

    public int getCostType() {
        return syncedData != null ? syncedData.costType() : 0;
    }

    public boolean isBuyOfferUsed() {
        return syncedData != null && syncedData.buyOfferUsed();
    }

    public boolean isSellOfferUsed() {
        return syncedData != null && syncedData.sellOfferUsed();
    }

    public ItemStack getBuyOfferCost() {
        int type = getCostType();
        return switch (type) {
            case 0 -> new ItemStack(Items.DIAMOND_BLOCK, 1);
            case 1 -> new ItemStack(Items.EMERALD_BLOCK, 1);
            default -> new ItemStack(Items.GOLDEN_APPLE, 5);
        };
    }

    public ItemStack getSellReward() {
        return syncedData != null ? syncedData.sellReward() : ItemStack.EMPTY;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        if (whisperServer != null) {
            return whisperServer.isAlive() && player.squaredDistanceTo(whisperServer) <= 64.0;
        }
        return player.getWorld().getEntityById(whisperEntityId) instanceof WhisperEntity;
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        if (player.getWorld().isClient) return true;

        WhisperEntity whisper = getServerWhisper(player);
        if (whisper == null) return false;

        if (id == 0) {
            processBuySoul(player, whisper);
        } else if (id == 1) {
            processSellSoul(player, whisper);
        }

        this.syncedData = new WhisperData(whisper.getId(), whisper.getCostType(), whisper.isBuyOfferUsed(), whisper.isSellOfferUsed(), whisper.getSellReward());
        return true;
    }

    private WhisperEntity getServerWhisper(PlayerEntity player) {
        if (whisperServer != null && whisperServer.isAlive()) return whisperServer;
        if (player.getWorld().getEntityById(whisperEntityId) instanceof WhisperEntity w) {
            whisperServer = w;
            return w;
        }
        return null;
    }

    private void processBuySoul(PlayerEntity player, WhisperEntity whisper) {
        if (whisper.isBuyOfferUsed()) return;
        ItemStack cost = whisper.getBuyOfferCost();

        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack slot = player.getInventory().getStack(i);
            if (ItemStack.areItemsEqual(slot, cost) && slot.getCount() >= cost.getCount()) {
                slot.decrement(cost.getCount());
                player.getInventory().offerOrDrop(new ItemStack(ModItems.SOUL));
                whisper.markBuyOfferUsed();
                return;
            }
        }
    }

    private void processSellSoul(PlayerEntity player, WhisperEntity whisper) {
        if (whisper.isSellOfferUsed()) return;

        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack slot = player.getInventory().getStack(i);
            if (slot.isOf(ModItems.SOUL)) {
                slot.decrement(1);
                player.getInventory().offerOrDrop(whisper.getSellReward());
                whisper.markSellOfferUsed();
                return;
            }
        }
    }

    public record WhisperData(int entityId, int costType, boolean buyOfferUsed, boolean sellOfferUsed, ItemStack sellReward) {
        public static final PacketCodec<RegistryByteBuf, WhisperData> PACKET_CODEC = PacketCodec.of(
                (value, buf) -> {
                    buf.writeInt(value.entityId);
                    buf.writeInt(value.costType);
                    buf.writeBoolean(value.buyOfferUsed);
                    buf.writeBoolean(value.sellOfferUsed);
                    ItemStack.PACKET_CODEC.encode(buf, value.sellReward);
                },
                buf -> new WhisperData(
                        buf.readInt(),
                        buf.readInt(),
                        buf.readBoolean(),
                        buf.readBoolean(),
                        ItemStack.PACKET_CODEC.decode(buf)
                )
        );
    }
}
