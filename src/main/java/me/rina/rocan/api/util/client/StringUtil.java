package me.rina.rocan.api.util.client;

import me.rina.turok.render.font.TurokFont;
import me.rina.turok.render.font.hal.CFontRenderer;

/**
 * @author SrRina
 * @since 18/03/2021 at 11:59
 **/
public class StringUtil {
    public static String trimStringToWidth(CFontRenderer font, String text, int width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;

        for(int l = j; l >= 0 && l < text.length() && i < width; l += k) {
            char c0 = text.charAt(l);
            int i1 = font.getStringWidth(Character.toString(c0));
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (i1 < 0) {
                flag = true;
            } else {
                i += i1;
                if (flag1) {
                    ++i;
                }
            }

            if (i > width) {
                break;
            }

            if (reverse) {
                stringbuilder.insert(0, c0);
            } else {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }
}
