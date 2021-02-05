package me.rina.rocan.api.util.math;

public class TimeUtil {
  private long time;

  public TimeUtil() {
    time = System.nanoTime() / 1000000L;
  }

  public boolean reach(final long time) {
    return time() >= time;
  }

  public void reset() {
    time = System.nanoTime() / 1000000L;
  }

  public long time() {
    return System.nanoTime() / 1000000L - time;
  }
}