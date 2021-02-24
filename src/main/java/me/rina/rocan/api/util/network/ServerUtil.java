package me.rina.rocan.api.util.network;

import net.minecraft.client.network.NetworkPlayerInfo;

/**
 * @author SrRina
 * @since 23/02/2021 at 20:17
 **/
public class ServerUtil {
    public static int getPing(NetworkPlayerInfo player) {
        return player.getResponseTime();
    }
}
