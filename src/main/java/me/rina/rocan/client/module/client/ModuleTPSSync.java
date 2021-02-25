package me.rina.rocan.client.module.client;

import me.rina.rocan.api.module.Module;
import me.rina.rocan.api.module.impl.ModuleCategory;
import me.rina.rocan.api.module.registry.Registry;
import me.rina.rocan.client.event.network.PacketEvent;
import me.rina.turok.Turok;
import me.rina.turok.util.TurokMath;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import team.stiff.pomelo.impl.annotated.handler.annotation.Listener;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author SrRina
 * @since 24/02/2021 at 12:15
 **/
@Registry(name = "TPS Sync", tag = "TPSSync", description = "Sync client actions with TPS.", category = ModuleCategory.CLIENT)
public class ModuleTPSSync extends Module {
    public static ModuleTPSSync INSTANCE;

    private static final float[] tickRates = new float[20];
    private long lastTick;

    private int index;

    public ModuleTPSSync() {
        INSTANCE = this;

        this.resetVariables();
    }

    public void setLastTick(long lastTick) {
        this.lastTick = lastTick;
    }

    public long getLastTick() {
        return lastTick;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Listener
    public void onListenPacketEvent(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            this.doSync();
        }
    }

    public void doSync() {
        if (this.lastTick != -1) {
            float currentTicks = (System.currentTimeMillis() - this.lastTick) / 1000.0f;
            tickRates[(this.index % tickRates.length)] = TurokMath.clamp(20.0f / currentTicks, 0f, 20);

            this.index += 1;
        }

        this.lastTick = System.currentTimeMillis();
    }

    public void resetVariables() {
        this.index = 0;
        this.lastTick = -1l;

        Arrays.fill(tickRates, 0f);
    }

    public static float getTPS() {
        float sum = 0f;
        float num = 0f;

        for (float ticks : tickRates) {
            if (ticks > 0f) {
                sum += ticks;
                num += 1f;
            }
        }

        return TurokMath.clamp(sum / num, 0f, 20f);
    }
}