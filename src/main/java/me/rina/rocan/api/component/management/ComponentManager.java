package me.rina.rocan.api.component.management;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.ISLClass;
import me.rina.rocan.api.component.Component;
import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.management.ModuleManager;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.api.util.chat.ChatUtil;
import me.rina.rocan.client.module.client.ModuleHUD;
import me.rina.turok.util.TurokDisplay;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import me.rina.turok.util.TurokTick;
import org.lwjgl.opengl.Display;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author SrRina
 * @since 23/03/2021 at 15:38
 **/
public class ComponentManager implements ISLClass {
    public static ComponentManager INSTANCE;

    /* The main list. */
    private ArrayList<Component> componentList;

    /* The docks list, works one array list HUD. */
    private ArrayList<Component> componentDockingTopLeft;
    private ArrayList<Component> componentDockingTopRight;
    private ArrayList<Component> componentDockingBottomLeft;
    private ArrayList<Component> componentDockingBottomRight;

    /* The rectangles of docking. */
    final private TurokRect rectDockingTopLeft = new TurokRect("Top Left", 0,0);
    final private TurokRect rectDockingTopRight = new TurokRect("Top Right", 0,0);
    final private TurokRect rectDockingBottomLeft = new TurokRect("Bottom Left", 0,0);
    final private TurokRect rectDockingBottomRight = new TurokRect("Bottom Right", 0,0);

    /* Offset to smooth animation. */
    private float offsetChat;

    private boolean wasResized;
    final private TurokTick timeoutResize = new TurokTick();

    public ComponentManager() {
        INSTANCE = this;

        this.componentList = new ArrayList<>();

        this.componentDockingTopLeft = new ArrayList<>();
        this.componentDockingTopRight = new ArrayList<>();
        this.componentDockingBottomLeft = new ArrayList<>();
        this.componentDockingBottomRight = new ArrayList<>();
    }

    public void setComponentList(ArrayList<Component> componentList) {
        this.componentList = componentList;
    }

    public ArrayList<Component> getComponentList() {
        return componentList;
    }

    public TurokRect getRectDockingTopLeft() {
        return rectDockingTopLeft;
    }

    public TurokRect getRectDockingTopRight() {
        return rectDockingTopRight;
    }

    public TurokRect getRectDockingBottomLeft() {
        return rectDockingBottomLeft;
    }

    public TurokRect getRectDockingBottomRight() {
        return rectDockingBottomRight;
    }

    public ArrayList<Component> getComponentDockingTopLeft() {
        return componentDockingTopLeft;
    }

    public ArrayList<Component> getComponentDockingTopRight() {
        return componentDockingTopRight;
    }

    public ArrayList<Component> getComponentDockingBottomLeft() {
        return componentDockingBottomLeft;
    }

    public ArrayList<Component> getComponentDockingBottomRight() {
        return componentDockingBottomRight;
    }

    public void registry(Component component) {
        try {
            for (Field fields : component.getClass().getDeclaredFields()) {
                if (Setting.class.isAssignableFrom(fields.getType())) {
                    if (!fields.isAccessible()) {
                        fields.setAccessible(true);
                    }

                    final Setting settingDeclared = (Setting) fields.get(component);

                    component.registry(settingDeclared);
                }
            }

            this.componentList.add(component);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public void unregister(Component component) {
        if (get(component.getClass()) != null) {
            this.componentList.remove(component);
        }
    }

    public static Component get(Class<?> clazz) {
        for (Component components : ComponentManager.INSTANCE.getComponentList()) {
            if (components.getClass() == clazz) {
                return components;
            }
        }

        return null;
    }

    public static Component get(String tag) {
        for (Component components : ComponentManager.INSTANCE.getComponentList()) {
            if (components.getTag().equalsIgnoreCase(tag)) {
                return components;
            }
        }

        return null;
    }

    public void applyDock() {
        TurokDisplay display = new TurokDisplay(mc);

        this.offsetChat = TurokMath.lerp(this.offsetChat, mc.world != null && mc.ingameGUI.getChatGUI().getChatOpen() ? 16 : 0, Rocan.getClientEventManager().getCurrentRender2DPartialTicks());

        // Make the resize very readable for client!
        if (Display.wasResized() && !this.wasResized) {
            this.wasResized = true;
            this.timeoutResize.reset();
        }

        // The hit time of state Display.wasResized() is very fast... or the tick, I really don't know!
        // But I used one timeout for this action!
        if (this.timeoutResize.isPassedMS(250)) {
            this.wasResized = false;
        }

        for (Component components : this.componentList) {
            if (this.wasResized) {
                continue;
            }

            if (components.isEnabled() && components.getRect().getDocking() == TurokRect.Dock.TOP_LEFT && this.rectDockingTopLeft.collideWithRect(components.getRect()) && get(components.getTag(), this.componentDockingTopLeft) == null) {
                this.componentDockingTopLeft.add(components);
            } else if (components.isEnabled() && components.getRect().getDocking() == TurokRect.Dock.TOP_LEFT && !this.rectDockingTopLeft.collideWithRect(components.getRect()) && get(components.getTag(), this.componentDockingTopLeft) != null) {
                this.componentDockingTopLeft.remove(components);
            } else if (components.isEnabled() && components.getRect().getDocking() != TurokRect.Dock.TOP_LEFT && get(components.getTag(), this.componentDockingTopLeft) != null) {
                this.componentDockingTopLeft.remove(components);
            } else if (!components.isEnabled() && get(components.getTag(), this.componentDockingTopLeft) != null) {
                this.componentDockingTopLeft.remove(components);
            }

            if (components.isEnabled() && components.getRect().getDocking() == TurokRect.Dock.TOP_RIGHT && this.rectDockingTopRight.collideWithRect(components.getRect()) && get(components.getTag(), this.componentDockingTopRight) == null) {
                this.componentDockingTopRight.add(components);
            } else if (components.isEnabled() && components.getRect().getDocking() == TurokRect.Dock.TOP_RIGHT && !this.rectDockingTopRight.collideWithRect(components.getRect()) && get(components.getTag(), this.componentDockingTopRight) != null) {
                this.componentDockingTopRight.remove(components);
            } else if (components.isEnabled() && components.getRect().getDocking() != TurokRect.Dock.TOP_RIGHT && get(components.getTag(), this.componentDockingTopRight) != null) {
                this.componentDockingTopRight.remove(components);
            } else if (!components.isEnabled() && get(components.getTag(), this.componentDockingTopRight) != null) {
                this.componentDockingTopRight.remove(components);
            }

            if (components.isEnabled() && components.getRect().getDocking() == TurokRect.Dock.BOTTOM_LEFT && this.rectDockingBottomLeft.collideWithRect(components.getRect()) && get(components.getTag(), this.componentDockingBottomLeft) == null) {
                this.componentDockingBottomLeft.add(components);
            } else if (components.isEnabled() && components.getRect().getDocking() == TurokRect.Dock.BOTTOM_LEFT && !this.rectDockingBottomLeft.collideWithRect(components.getRect()) && get(components.getTag(), this.componentDockingBottomLeft) != null) {
                this.componentDockingBottomLeft.remove(components);
            } else if (components.isEnabled() && components.getRect().getDocking() != TurokRect.Dock.BOTTOM_LEFT && get(components.getTag(), this.componentDockingBottomLeft) != null) {
                this.componentDockingBottomLeft.remove(components);
            } else if (!components.isEnabled() && get(components.getTag(), this.componentDockingBottomLeft) != null) {
                this.componentDockingBottomLeft.remove(components);
            }

            if (components.isEnabled() && components.getRect().getDocking() == TurokRect.Dock.BOTTOM_RIGHT && this.rectDockingBottomRight.collideWithRect(components.getRect()) && get(components.getTag(), this.componentDockingBottomRight) == null) {
                this.componentDockingBottomRight.add(components);
            } else if (components.isEnabled() && components.getRect().getDocking() == TurokRect.Dock.BOTTOM_RIGHT && !this.rectDockingBottomRight.collideWithRect(components.getRect()) && get(components.getTag(), this.componentDockingBottomRight) != null) {
                this.componentDockingBottomRight.remove(components);
            } else if (components.isEnabled() && components.getRect().getDocking() != TurokRect.Dock.BOTTOM_RIGHT && get(components.getTag(), this.componentDockingBottomRight) != null) {
                this.componentDockingBottomRight.remove(components);
            } else if (!components.isEnabled() && get(components.getTag(), this.componentDockingBottomRight) != null) {
                this.componentDockingBottomRight.remove(components);
            }
        }

        /* The top left update. */
        float cacheTopLeft = 1;

        for (Component components : this.componentDockingTopLeft) {
            if (!components.isEnabled()) {
                continue;
            }

            if (!components.isDragging()) {
                components.getRect().setX(TurokMath.lerp(components.getRect().getX(), this.rectDockingTopLeft.getX(), Rocan.getClientEventManager().getCurrentRender2DPartialTicks()));
                components.getRect().setY(TurokMath.lerp(components.getRect().getY(), cacheTopLeft, Rocan.getClientEventManager().getCurrentRender2DPartialTicks()));
            }

            cacheTopLeft = components.getRect().getY() + components.getRect().getHeight() + 1;
        }

        this.rectDockingTopLeft.setX(1);
        this.rectDockingTopLeft.setY(1);

        this.rectDockingTopLeft.setWidth(10);
        this.rectDockingTopLeft.setHeight(cacheTopLeft);

        /* The top right update. */
        float cacheTopRight = 1f;

        for (Component components : this.componentDockingTopRight) {
            if (!components.isEnabled()) {
                continue;
            }

            if (!components.isDragging()) {
                components.getRect().setX(TurokMath.lerp(components.getRect().getX(),display.getScaledWidth() - components.getRect().getWidth() - 1, Rocan.getClientEventManager().getCurrentRender2DPartialTicks()));
                components.getRect().setY(TurokMath.lerp(components.getRect().getY(), cacheTopRight, Rocan.getClientEventManager().getCurrentRender2DPartialTicks()));
            }

            cacheTopRight = components.getRect().getY() + components.getRect().getHeight() + 1;
        }

        this.rectDockingTopRight.setX(display.getScaledWidth() - this.rectDockingTopLeft.getWidth() - 1);
        this.rectDockingTopRight.setY(1);

        this.rectDockingTopRight.setWidth(10);
        this.rectDockingTopRight.setHeight(cacheTopRight);

        /* The bottom left update. */
        float cacheBottomLeft = display.getScaledHeight() - this.offsetChat - 1;

        for (Component components : this.componentDockingBottomLeft) {
            if (!components.isEnabled()) {
                continue;
            }

            if (!components.isDragging()) {
                components.getRect().setX(TurokMath.lerp(components.getRect().getX(), this.rectDockingBottomLeft.getX(), Rocan.getClientEventManager().getCurrentRender2DPartialTicks()));
                components.getRect().setY(TurokMath.lerp(components.getRect().getY(), cacheBottomLeft - components.getRect().getHeight(), Rocan.getClientEventManager().getCurrentRender2DPartialTicks()));
            }

            cacheBottomLeft = components.getRect().getY() - 1;
        }

        this.rectDockingBottomLeft.setX(1);
        this.rectDockingBottomLeft.setY(cacheBottomLeft);

        this.rectDockingBottomLeft.setWidth(10);
        this.rectDockingBottomLeft.setHeight(display.getScaledHeight() - cacheBottomLeft);

        /* The most terrible... joke, the bottom right update!. */
        float cacheBottomRight = display.getScaledHeight() - this.offsetChat - 1;

        for (Component components : this.componentDockingBottomRight) {
            if (!components.isEnabled()) {
                continue;
            }

            if (!components.isDragging()) {
                components.getRect().setX(TurokMath.lerp(components.getRect().getX(), display.getScaledWidth() - components.getRect().getWidth() - 1, Rocan.getClientEventManager().getCurrentRender2DPartialTicks()));
                components.getRect().setY(TurokMath.lerp(components.getRect().getY(), cacheBottomRight - components.getRect().getHeight(), Rocan.getClientEventManager().getCurrentRender2DPartialTicks()));
            }

            cacheBottomRight = components.getRect().getY() - 1;
            this.rectDockingBottomRight.height = components.getRect().getY() + components.getRect().getHeight() + 1;
        }

        this.rectDockingBottomRight.setX(display.getScaledWidth() - this.rectDockingBottomRight.getWidth() - 1);
        this.rectDockingBottomRight.setY(cacheBottomRight);

        this.rectDockingBottomRight.setWidth(10);
        this.rectDockingBottomRight.setHeight(display.getScaledHeight() - cacheBottomRight);
    }

    public static Component get(String tag, ArrayList<Component> theList) {
        Component component = null;

        for (Component components : theList) {
            if (components.getTag().equalsIgnoreCase(tag)) {
                component = components;

                break;
            }
        }

        return component;
    }

    @Override
    public void onSave() {

    }

    @Override
    public void onLoad() {

    }
}
