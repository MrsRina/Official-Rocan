package me.rina.rocan.api.tracker.management;

import me.rina.rocan.api.tracker.Tracker;

import java.util.ArrayList;

/**
 * @author SrRina
 * @since 05/02/2021 at 13:02
 **/
public class TrackerManager {
    public static final float DEFAULT_DELAY = 0f;
    public static TrackerManager INSTANCE;

    private ArrayList<Tracker> trackerList;

    public TrackerManager() {
        INSTANCE = this;

        this.trackerList = new ArrayList<>();
    }

    public void setTrackerList(ArrayList<Tracker> trackerList) {
        this.trackerList = trackerList;
    }

    public ArrayList<Tracker> getTrackerList() {
        return trackerList;
    }

    public void registry(Tracker tracker) {
        this.trackerList.add(tracker);
    }

    public void unregister(Tracker tracker) {
        if (get(tracker.getClass()) == null) {
            return;
        }

        this.trackerList.remove(tracker);
    }

    public static Tracker get(Class<?> clazz) {
        for (Tracker trackers : INSTANCE.getTrackerList()) {
            if (trackers.getClass() == clazz) {
                return trackers;
            }
        }

        return null;
    }

    public static Tracker get(String name) {
        for (Tracker trackers : INSTANCE.getTrackerList()) {
            if (trackers.getName().equalsIgnoreCase(name)) {
                return trackers;
            }
        }

        return null;
    }

    public void onUpdateAll() {
        for (Tracker trackers : INSTANCE.getTrackerList()) {
            if (trackers.isRegistry()) {
                trackers.onUpdate();
            }
        }
    }
}
