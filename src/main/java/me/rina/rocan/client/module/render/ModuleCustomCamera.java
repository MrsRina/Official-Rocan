package me.rina.rocan.client.module.render;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.api.util.client.NullUtil;
import me.rina.rocan.client.event.client.ClientTickEvent;
import me.rina.rocan.client.event.render.EnumHandSideEvent;
import me.rina.turok.render.opengl.TurokGL;
import net.minecraft.util.EnumHandSide;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

/**
 * @author Jake
 * @author SrRina
 *
 * Sorry jake, I have update the module.
 **/
@Registry(name = "Custom Camera", tag = "CustomCamera", description = "Manage camera stuff and player hands.", category = ModuleCategory.RENDER)
public class ModuleCustomCamera extends Module {
    /* Field of View. */
    public static ValueNumber settingFieldOfView = new ValueNumber("Field of View", "FieldOfView", "Field of view camera.", 130, 0, 180);

    /* Misc right hand. */
    public static ValueNumber settingRightX = new ValueNumber("Right X", "RightX", "Changes the x value.", 0.0, -50.0, 50.0);
    public static ValueNumber settingRightY = new ValueNumber("Right Y", "RightY", "Changes the y value.", 0.0, -50.0, 50.0);
    public static ValueNumber settingRightZ = new ValueNumber("Right Z", "RightZ", "Changes the z value.", 0.0, -50.0, 50.0);

    public static ValueNumber settingScaleRight = new ValueNumber("Scale Right", "ScaleRight", "Changes the scale.", 10, 0, 50);

    /* Misc left hand. */
    public static ValueNumber settingLeftX = new ValueNumber("Left X", "LeftX", "Changes the x value.", 0.0, -50.0, 50.0);
    public static ValueNumber settingLeftY = new ValueNumber("Left Y", "LeftY", "Changes the y value.", 0.0, -50.0, 50.0);
    public static ValueNumber settingLeftZ = new ValueNumber("Left Z", "LeftZ", "Changes the z value.", 0.0, -50.0, 50.0);

    public static ValueNumber settingScaleLeft = new ValueNumber("Scale Left", "ScaleLeft", "Changes the scale.", 10, 0, 50);

    @Listener
    public void onListenTickEvent(ClientTickEvent event) {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        mc.gameSettings.fovSetting = settingFieldOfView.getValue().intValue();
    }

    @Listener
    public void onEnumHandSideEvent(EnumHandSideEvent event) {
        switch (event.getHandSide()) {
            case LEFT: {
                TurokGL.translate(settingRightX.getValue().floatValue() / 100, settingRightY.getValue().floatValue() / 100, settingRightZ.getValue().floatValue() / 100);
                TurokGL.scale(settingScaleRight.getValue().floatValue() / 10, settingScaleRight.getValue().floatValue() / 10, settingScaleRight.getValue().floatValue() / 10);

                break;
            }

            case RIGHT: {
                TurokGL.translate(settingLeftX.getValue().floatValue() / 100, settingLeftY.getValue().floatValue() / 100, settingLeftZ.getValue().floatValue() / 100);
                TurokGL.scale(settingScaleLeft.getValue().floatValue() / 10, settingScaleLeft.getValue().floatValue() / 10, settingScaleLeft.getValue().floatValue() / 10);
            }
        }
    }
}
