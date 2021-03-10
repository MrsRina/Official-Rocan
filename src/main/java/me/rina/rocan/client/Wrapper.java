package me.rina.rocan.client;

import me.rina.turok.render.font.TurokFont;

import java.awt.*;

/**
 * @author SrRina
 * @since 07/12/20 at 02:58pm
 */
public class Wrapper {
    public TurokFont fontBigWidget = new TurokFont(new Font("Whitney", 0, 18), true, true);
    public TurokFont fontNormalWidget = new TurokFont(new Font("Whitney", 0, 19), true, true);
    public TurokFont fontSmallWidget = new TurokFont(new Font("Whitney", 0, 16), true, true);
    public TurokFont fontNameTags = new TurokFont(new Font("Whitney", 0, 19), true, true);

    public int[] colorFrameBackground = new int[] {
            0, 0, 0, 150
    };

    public int[] colorContainerBackground = new int[] {
            0, 0, 0, 200
    };

    public int[] colorWidgetHighlight = new int[] {
            255, 255, 255, 100
    };

    public int[] colorWidgetPressed = new int[] {
            0, 0, 255, 100
    };

    public int[] colorWidgetSelected = new int[] {
            255, 255, 255, 100
    };

    public int[] colorWidgetBackground = new int[] {
            0, 0, 0, 100
    };

    public void onUpdateColor() {
        // We need update the colors lists here using settings colors.
        // The GUI is not done.

        this.colorFrameBackground = new int[] {
                0, 0, 0, 150
        };

        this.colorContainerBackground = new int[] {
                0, 0, 0, 100
        };

        this.colorWidgetHighlight = new int[] {
                255, 255, 255, 100
        };

        this.colorWidgetPressed = new int[] {
                0, 0, 255, 100
        };

        this.colorWidgetSelected = new int[] {
                255, 255, 255, 100
        };
    }
}