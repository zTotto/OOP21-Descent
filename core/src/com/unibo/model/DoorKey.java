package com.unibo.model;

/**
 * Class for the key that opens each level door.
 */
public class DoorKey extends Item {

    private DoorKey(final String name, final String id) {
        super(name, id);
    }

    /**
     * Constructor for the key.
     */
    public DoorKey() {
        this("Magic Key", "0");
    }
}
