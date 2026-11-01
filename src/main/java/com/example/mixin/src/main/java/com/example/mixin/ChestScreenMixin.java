package com.example.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class ChestScreenMixin<T extends ScreenHandler> extends Screen {
    @Shadow protected T handler;
    @Shadow protected int x;
    @Shadow protected int y;
    @Shadow protected abstract void onMouseClick(net.minecraft.screen.slot.Slot slot, int slotId, int button, SlotActionType actionType);

    protected ChestScreenMixin(Text title) { super(title); }

    @Inject(method = "init", at = @At("TAIL"))
    private void initButtons(CallbackInfo ci) {
        int btnX = this.x - 105;
        int btnY = this.y + 10;

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Herşeyi Al"), b -> takeAll()).dimensions(btnX, btnY, 100, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Herşeyi Koy"), b -> putAll()).dimensions(btnX, btnY + 24, 100, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Herşeyi At"), b -> dropAll()).dimensions(btnX, btnY + 48, 100, 20).build());
    }

    private void takeAll() {
        int containerSlots = this.handler.slots.size() - 36;
        for (int i = 0; i < containerSlots; i++) {
            if (this.handler.slots.get(i).hasStack()) {
                this.onMouseClick(this.handler.slots.get(i), i, 0, SlotActionType.QUICK_MOVE);
            }
        }
    }

    private void putAll() {
        int totalSlots = this.handler.slots.size();
        int containerSlots = totalSlots - 36;
        for (int i = containerSlots; i < totalSlots; i++) {
            if (this.handler.slots.get(i).hasStack()) {
                this.onMouseClick(this.handler.slots.get(i), i, 0, SlotActionType.QUICK_MOVE);
            }
        }
    }

    private void dropAll() {
        int containerSlots = this.handler.slots.size() - 36;
        for (int i = 0; i < containerSlots; i++) {
            if (this.handler.slots.get(i).hasStack()) {
                this.onMouseClick(this.handler.slots.get(i), i, 1, SlotActionType.THROW);
            }
        }
    }
}
