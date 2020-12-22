package me.rina.rocan.client.gui.module.setting.container;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.gui.container.Container;
import me.rina.rocan.api.gui.flag.Flag;
import me.rina.rocan.api.gui.widget.Widget;
import me.rina.rocan.api.setting.Setting;
import me.rina.rocan.client.gui.module.ModuleClickGUI;
import me.rina.rocan.client.gui.module.module.container.ModuleContainer;
import me.rina.rocan.client.gui.module.module.widget.ModuleCategoryWidget;
import me.rina.rocan.client.gui.module.module.widget.ModuleWidget;
import me.rina.rocan.client.gui.module.mother.MotherFrame;
import me.rina.rocan.client.gui.module.setting.widget.SettingBooleanWidget;
import me.rina.rocan.client.gui.module.visual.LabelWidget;
import me.rina.turok.render.opengl.TurokRenderGL;
import me.rina.turok.util.TurokMath;
import me.rina.turok.util.TurokRect;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 2020-12-14 at 21:24
 **/
public class SettingContainer extends Container {
    private ModuleClickGUI master;
    private MotherFrame frame;

    private ModuleCategoryWidget widgetCategory;
    private ModuleContainer container;

    private ModuleWidget widgetModule;

    private int offsetX;
    private int offsetY;

    private int offsetWidth;
    private int offsetHeight;

    private ArrayList<Widget> loadedWidgetList;

    /*
     * We need create the label that is the description of the module in the container.
     * Later we will need use this to get the height and reset height scroll, so, easy concept.
     */
    private LabelWidget descriptionLabel;

    private TurokRect scrollRect = new TurokRect("I will go to canada and forget everything make me bad.", 0, 0);

    public Flag flagMouse = Flag.MouseNotOver;
    public Flag flagDescription = Flag.MouseNotOver;

    public SettingContainer(ModuleClickGUI master, MotherFrame frame, ModuleCategoryWidget widgetCategory, ModuleContainer container, ModuleWidget widgetModule) {
        super(widgetModule.getModule().getTag());

        this.master = master;
        this.frame = frame;

        this.widgetCategory = widgetCategory;
        this.container = container;

        this.widgetModule = widgetModule;

        this.init();
    }

    public void init() {
        if (this.loadedWidgetList == null) {
            this.loadedWidgetList = new ArrayList<>();
        } else {
            this.loadedWidgetList.clear();
        }

        this.descriptionLabel = new LabelWidget(this.master, this.frame, this.widgetCategory, this.container, this.widgetModule, this, this.widgetModule.getModule().getDescription());

        this.scrollRect.setHeight(0);
        this.offsetY = 3;

        this.descriptionLabel.setOffsetY(this.scrollRect.getHeight());

        this.loadedWidgetList.add(this.descriptionLabel);

        this.scrollRect.height += this.descriptionLabel.getRect().getHeight() + 1;

        for (Setting settings : this.widgetModule.getModule().getSettingList()) {
            if (settings.getValue() instanceof Boolean) {
                SettingBooleanWidget settingBooleanWidget = new SettingBooleanWidget(this.master, this.frame, this.widgetCategory, this.container, this.widgetModule, this, settings);

                settingBooleanWidget.setOffsetY(this.scrollRect.getHeight());

                this.loadedWidgetList.add(settingBooleanWidget);

                this.scrollRect.height += settingBooleanWidget.getRect().getHeight() + 1;
            }
        }
    }

    public void onRefreshWidget() {
        // Set 0 the start up height value.
        this.scrollRect.setHeight(0);

        // Clear the list.
        this.loadedWidgetList.clear();

        // Set the last save y offset.
        this.descriptionLabel.setOffsetY(this.scrollRect.getHeight());

        // Put in the loaded widget list to enable render.
        this.loadedWidgetList.add(this.descriptionLabel);

        // Update height with the height of description label.
        this.scrollRect.height += this.descriptionLabel.getRect().getHeight() + 1;

        for (Setting settings : this.widgetModule.getModule().getSettingList()) {
            if (!settings.isEnabled()) {
                continue;
            }

            if (settings.getValue() instanceof Boolean) {
                SettingBooleanWidget settingBooleanWidget = new SettingBooleanWidget(this.master, this.frame, this.widgetCategory, this.container, this.widgetModule, this, settings);

                settingBooleanWidget.setOffsetY(this.scrollRect.getHeight());

                this.loadedWidgetList.add(settingBooleanWidget);

                this.scrollRect.height += settingBooleanWidget.getRect().getHeight() + 1;
            }
        }
    }

    protected void setWidgetModule(ModuleWidget widgetModule) {
        this.widgetModule = widgetModule;
    }

    public ModuleWidget getWidgetModule() {
        return widgetModule;
    }

    public void setDescriptionLabel(LabelWidget descriptionLabel) {
        this.descriptionLabel = descriptionLabel;
    }

    public LabelWidget getDescriptionLabel() {
        return descriptionLabel;
    }

    public int getWidthScale() {
        int currentScaleX = (5 * this.frame.getScale());
        int scale = (2 * this.frame.getScale());

        return (this.frame.getRect().getWidth() - this.container.getRect().getWidth() - currentScaleX - scale + 1);
    }

    public void setScrollRect(TurokRect scrollRect) {
        this.scrollRect = scrollRect;
    }

    public TurokRect getScrollRect() {
        return scrollRect;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetWidth(int offsetWidth) {
        this.offsetWidth = offsetWidth;
    }

    public int getOffsetWidth() {
        return offsetWidth;
    }

    public void setOffsetHeight(int offsetHeight) {
        this.offsetHeight = offsetHeight;
    }

    public int getOffsetHeight() {
        return offsetHeight;
    }

    @Override
    public void onKeyboard(char character, int key) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onKeyboard(character, key);
        }
    }

    @Override
    public void onCustomKeyboard(char character, int key) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomKeyboard(character, key);
        }
    }

    @Override
    public void onMouseReleased(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onMouseReleased(button);
        }
    }

    @Override
    public void onCustomMouseReleased(int button) {
        if (this.widgetModule.isSelected()) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onCustomMouseReleased(button);
            }
        }
    }

    @Override
    public void onMouseClicked(int button) {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onMouseClicked(button);
        }
    }

    @Override
    public void onCustomMouseClicked(int button) {
        if (this.widgetModule.isSelected()) {
            for (Widget widgets : this.loadedWidgetList) {
                widgets.onCustomMouseClicked(button);
            }
        }
    }

    public void onCustomRender() {
        for (Widget widgets : this.loadedWidgetList) {
            widgets.onCustomRender();
        }
    }

    @Override
    public void onRender() {
        int positionXScaled = (this.container.getRect().getX() + this.container.getRect().getWidth()) + (2 * this.frame.getScale());
        int positionYScaled = this.container.getRect().getY();

        this.rect.setX(positionXScaled);
        this.rect.setY(positionYScaled);

        this.scrollRect.setX(this.rect.getX());
        this.scrollRect.setY((int) TurokMath.linearInterpolation(this.scrollRect.getY(), this.rect.getY() + this.offsetY, this.master.getPartialTicks()));

        int offsetFixOutline = 1;

        if (this.widgetModule.isSelected()) {
            this.flagMouse = this.rect.collideWithMouse(this.master.getMouse()) ? Flag.MouseOver : Flag.MouseNotOver;

            TurokRenderGL.color(Rocan.getGUI().colorContainerBackground[0], Rocan.getGUI().colorContainerBackground[1], Rocan.getGUI().colorContainerBackground[2], Rocan.getGUI().colorContainerBackground[3]);
            TurokRenderGL.drawSolidRect(this.rect);

            for (Widget widgets : this.loadedWidgetList) {
                widgets.onRender();
            }

            TurokRenderGL.enable(GL11.GL_SCISSOR_TEST);
            TurokRenderGL.drawScissor(this.rect.getX(), this.rect.getY() - offsetFixOutline, this.rect.getWidth() + (offsetFixOutline * 2), this.rect.getHeight());

            TurokRenderGL.disable(GL11.GL_SCISSOR_TEST);
        } else {
            this.flagMouse = Flag.MouseNotOver;
        }

        if (this.flagDescription == Flag.MouseNotOver) {
            this.descriptionLabel.setText(this.widgetModule.getModule().getDescription());
        }

        this.flagDescription = Flag.MouseNotOver;
    }
}
