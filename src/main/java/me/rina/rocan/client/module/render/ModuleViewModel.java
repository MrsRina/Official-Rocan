package me.rina.rocan.client.module.render;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.setting.value.ValueBoolean;
import me.rina.rocan.api.setting.value.ValueNumber;
import me.rina.rocan.client.event.render.EnumHandSideEvent;
import me.rina.turok.render.opengl.TurokGL;
import net.minecraft.util.EnumHandSide;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

public class ModuleViewModel extends Module {

    public ModuleViewModel() {
        super("Item Fov", "ItemFov", "changes the fov of the item held in your hand.", ModuleCategory.RENDER);
    }

    public static ValueNumber Rightx = new ValueNumber("Right X", "RightX", "Changes the x value", 0.0, -50.0, 50.0);
    public static ValueNumber Righty = new ValueNumber("Right Y", "RightY", "Changes the y value", 0.0, -50.0, 50.0);
    public static ValueNumber Rightz = new ValueNumber("Right Z", "RightZ", "Changes the z value", 0.0, -50.0, 50.0);
    public static ValueNumber Rights = new ValueNumber("Scale Right", "ScaleRight", "Changes the scale", 10, 0, 50);
    public static ValueNumber Leftx = new ValueNumber("Left X", "LeftX", "Changes the x value", 0.0, -50.0, 50.0);
    public static ValueNumber Lefty = new ValueNumber("Left Y", "LeftY", "Changes the y value", 0.0, -50.0, 50.0);
    public static ValueNumber Leftz = new ValueNumber("Left Z", "LeftZ", "Changes the z value", 0.0, -50.0, 50.0);
    public static ValueNumber Lefts = new ValueNumber("Scale Left", "ScaleLeft", "Changes the scale", 10, 0, 50);

    @Listener
    public void onEnumHandSideEvent(EnumHandSideEvent event) {
        if (event.getHandSide() == EnumHandSide.RIGHT) {
            TurokGL.translate(Rightx.getValue().floatValue() / 100, Righty.getValue().floatValue() / 100, Rightz.getValue().floatValue() / 100);
            TurokGL.scale(Rights.getValue().floatValue() / 10, Rights.getValue().floatValue() / 10, Rights.getValue().floatValue() / 10);
        } else if (event.getHandSide() == EnumHandSide.LEFT) {
            TurokGL.translate(Leftx.getValue().floatValue() / 100, Lefty.getValue().floatValue() / 100, Leftz.getValue().floatValue() / 100);
            TurokGL.scale(Lefts.getValue().floatValue() / 10, Lefts.getValue().floatValue() / 10, Lefts.getValue().floatValue() / 10);
        }
    }

}
