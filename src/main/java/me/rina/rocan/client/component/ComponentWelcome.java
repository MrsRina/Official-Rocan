package me.rina.rocan.client.component;

import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.component.impl.ComponentType;
import me.rina.rocan.api.component.registry.Registry;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.util.client.NullUtil;

/**
 * @author SrRina
 * @since 01/04/2021 at 00:31
 **/
@Registry(name = "Welcome", tag = "Welcome", description = "Cool welcomes!")
public class ComponentWelcome extends Component {
    /* The first setting for HUD in Rocan! */
    public static ValueBoolean settingWelcomeDay = new ValueBoolean("Welcome Day", "WelcomeDay", "Shows whats the welcome by hour day.", false);

    @Override
    public void onRenderHUD(float partialTicks) {
        if (NullUtil.isPlayer()) {
            return;
        }

        String text = "Welcome " + mc.player.getName() + "!";

        // Render the String!
        this.render(text, 0, 0);

        /* Set the sizes. */
        this.rect.setWidth(this.getStringWidth(text));
        this.rect.setHeight(this.getStringHeight(text));
    }
}