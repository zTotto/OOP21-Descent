package com.unibo.util;

/**
 * Enumeration for the weapon types.
 *
 */
public enum WeaponStats {
    /**
     * Longsword: Damage: 10 Range: 64.
     */
    LONGSWORD("Longsword", 10, 80),
    /**
     * Greataxe: Damage: 15 Range: 48.
     */
    GREATAXE("Greataxe", 15, 64),
    /**
     * Fists: Damage: 4 Range: 48.
     */
    FISTS("Fists", 4, 48);

    private final String name;
    private final int damage;
    private final int range;

    /**
     * Constructor for the enumeration WeaponStats.
     * 
     * @param name   of the weapon
     * @param damage of the weapon
     * @param range  of the weapon
     */
    WeaponStats(final String name, final int damage, final int range) {
        this.name = name;
        this.damage = damage;
        this.range = range;
    }

    /**
     * @return the name of the weapon
     */
    public String getName() {
        return name;
    }

    /**
     * @return the damage of the weapon
     */
    public int getDamage() {
        return damage;
    }

    /**
     * @return the range of the weapon
     */
    public int getRange() {
        return range;
    }

}
