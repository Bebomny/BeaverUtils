package dev.bebomny.beaver.beaverutils.mixins;

import dev.bebomny.beaver.beaverutils.configuration.gui.BeaverUtilsOptionsButtonWidget;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {


    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "initWidgets()V")
    private void initWidgets(CallbackInfo ci) {
        List<ClickableWidget> buttons = Screens.getButtons(this);
        buttons.add(new BeaverUtilsOptionsButtonWidget(this));
    }

}
