package dev.bebomny.beaver.beaverutils.features.features.autoclicker;

import dev.bebomny.beaver.beaverutils.mixinterface.IMinecraftClientInvoker;
import dev.bebomny.beaver.beaverutils.notifications.Notification;
import net.minecraft.text.Text;

public class RightMouseClickActionImpl extends MouseClickAction {

    protected RightMouseClickActionImpl(int delay, ClickType type) {
        super(MouseButton.RIGHT, delay, type);
    }


    @Override
    protected void performClickAction() {
        ((IMinecraftClientInvoker) client).invokeDoItemUse();
        //TODO: Send notification! maybe in the MouseClickAction class?
        notifier.newNotification(Notification.builder(Text.translatable("feature.auto_clicker.click.used_item.text"))
                .parent(Text.translatable("feature.text"))
                .duration(10)
                .build());
    }

    @Override
    protected void performHoldAction() {
        client.options.useKey.setPressed(true);
        notifier.newNotification(Notification.builder(Text.translatable("feature.auto_clicker.hold.use_item.text"))
                .parent(Text.translatable("feature.text"))
                .duration(10)
                .build());
    }
}
