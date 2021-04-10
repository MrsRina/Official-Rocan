package me.rina.rocan.client.component;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.rina.rocan.Rocan;
import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.component.impl.ComponentType;
import me.rina.rocan.api.component.registry.Registry;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.setting.value.ValueBoolean;

/**
 * @author SrRina
 * @since 04/04/2021 at 18:25
 **/
@Registry(name = "Watermark", tag = "Watermark", description = "Show client watermark")
public class ComponentWatermark extends Component {
    /* Misc settings. */
    public static ValueBoolean settingVersion = new ValueBoolean("Version", "Version", "Show version clients.", true);

    @Override
    public void onRenderHUD(float partialTicks) {
        String text = Rocan.NAME + " " + ChatFormatting.DARK_GRAY + (settingVersion.getValue() ? Rocan.VERSION : "");

        this.render(text, 0, 0);

        this.rect.setWidth(this.getStringWidth(text));
        this.rect.setHeight(this.getStringHeight(text));
    }
}
