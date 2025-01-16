package dev.bebomny.beaver.beaverutils.features.features.autoclicker;

import dev.bebomny.beaver.beaverutils.mixinterface.IMinecraftClientInvoker;
import dev.bebomny.beaver.beaverutils.notifications.Notification;
import net.minecraft.text.Text;

public class LeftMouseClickActionImpl extends MouseClickAction {

    public LeftMouseClickActionImpl(int delay, ClickType type) {
        super(MouseButton.LEFT, delay, type);
    }

    @Override
    protected void performClickAction() {
        ((IMinecraftClientInvoker) client).invokeDoAttack();
        notifier.newNotification(Notification.builder(Text.translatable("feature.auto_clicker.click.attacked.text"))
                .parent(Text.translatable("feature.text"))
                .duration(10)
                .build());
    }

    @Override
    protected void performHoldAction() {
        client.options.attackKey.setPressed(true);
        notifier.newNotification(Notification.builder(Text.translatable("feature.auto_clicker.hold.attack.text"))
                .parent(Text.translatable("feature.text"))
                .duration(10)
                .build());
    }
}
