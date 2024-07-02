package dev.bebomny.beaver.beaverutils.configuration.gui.buttons;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FeatureOptionsButtonWidget extends ButtonWidget {

    private static final Identifier optionsIcon = new Identifier("beaverutils", "options_icon.png");

    public FeatureOptionsButtonWidget(int x, int y, ButtonWidget.PressAction action) {
        super(x, y, 20, 20, Text.of("du"), action, DEFAULT_NARRATION_SUPPLIER);
    }

    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        //super.renderButton(context, mouseX, mouseY, delta);

        IconLocation iconLocation;
        iconLocation = isSelected() ? IconLocation.HOVER : IconLocation.NO_HOVER;

        context.drawTexture(optionsIcon, this.getX(), this.getY(), iconLocation.getU(), iconLocation.getV(), this.width, this.height, 20, 40);
    }


    static enum IconLocation {
        NO_HOVER(0, 0),
        HOVER(0, 20);

        private final int u;
        private final int v;

        private IconLocation(int u, int v) {
            this.u = u;
            this.v = v;
        }

        public int getU() {
            return u;
        }

        public int getV() {
            return v;
        }
    }
}
