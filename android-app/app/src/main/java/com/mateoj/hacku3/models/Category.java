package com.mateoj.hacku3.models;

/**
 * Created by jose on 2/12/15.
 */
public class Category {
    private String name;
    private String id;

    /**
     * Gets id.
     *
     * @return Value of id.
     */
    public String getId() {
        return id;
    }

    /**
     * Gets name.
     *
     * @return Value of name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets new name.
     *
     * @param name New value of name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets new id.
     *
     * @param id New value of id.
     */
    public void setId(String id) {
        this.id = id;
    }
}
