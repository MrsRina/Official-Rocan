package me.rina.rocan.api.manager;

/**
 * @author SrRina
 * @since 04/02/2021 at 19:07
 **/
public class Manager {
    private String name;
    private String description;

    public Manager(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
