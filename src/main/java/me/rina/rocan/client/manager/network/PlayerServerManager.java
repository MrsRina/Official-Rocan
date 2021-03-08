package me.rina.rocan.client.manager.network;

import me.rina.rocan.Rocan;
import me.rina.rocan.api.manager.Manager;
import me.rina.rocan.api.util.client.NullUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 19/02/2021 at 11:51
 **/
public class PlayerServerManager extends Manager {
    public static PlayerServerManager INSTANCE;

    private ArrayList<NetworkPlayerInfo> onlineList;

    public PlayerServerManager() {
        super("Player Server", "An manager with all players online on server.");

        INSTANCE = this;

        this.onlineList = new ArrayList<>();
    }

    public void setOnlineList(ArrayList<NetworkPlayerInfo> onlineList) {
        this.onlineList = onlineList;
    }

    public ArrayList<NetworkPlayerInfo> getOnlineList() {
        return onlineList;
    }

    public static NetworkPlayerInfo get(String name) {
        for (NetworkPlayerInfo playersInfo : INSTANCE.getOnlineList()) {
            if (playersInfo == null) {
                continue;
            }

            if (playersInfo.getGameProfile().getName().equalsIgnoreCase(name)) {
                return playersInfo;
            }
        }

        return null;
    }

    public static boolean isOnline() {
        final Minecraft mc = Rocan.getMinecraft();

        return mc.getConnection() != null;
    }

    @Override
    public void onUpdateAll() {
        if (NullUtil.isPlayerWorld()) {
            return;
        }

        if (mc.getConnection().getPlayerInfoMap() == null) {
            return;
        }

        this.onlineList.addAll(mc.getConnection().getPlayerInfoMap());
    }
}
