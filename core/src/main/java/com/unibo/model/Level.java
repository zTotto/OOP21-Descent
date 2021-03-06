package com.unibo.model;

import java.util.LinkedList;
import java.util.List;

import com.unibo.maps.DescentMap;
import com.unibo.model.items.ConsumableItem;
import com.unibo.model.items.Item;
import com.unibo.model.items.Weapon;
import com.unibo.util.Pair;
import com.unibo.util.Position;

/**
 * Class that models a level with a list of ConsumableItems, Weapons and
 * Enemies.
 */
public class Level {

    private final List<Item> items;
    private final List<Mob> enemies;
    private Position doorPosition = new Position(0, 0);
    private final List<Position> deadMobPositions;
    private final Pair<DescentMap, Float> map;

    /**
     * Constructor for a level that requires a map.
     * 
     * @param mapToAdd
     */
    public Level(final Pair<DescentMap, Float> mapToAdd) {
        items = new LinkedList<>();
        enemies = new LinkedList<>();
        deadMobPositions = new LinkedList<>();
        map = mapToAdd;
    }

    /**
     * Adds items to the level.
     * 
     * @param itemsToAdd items to add to the level
     * @return this level in order to chain calls
     */
    public Level addItems(final Item... itemsToAdd) {
        for (final Item i : itemsToAdd) {
            if (!i.isPosNull()) {
                items.add(i);
            }
        }
        return this;
    }

    /**
     * Adds enemies to the level.
     * 
     * @param enemies
     * @return this level in order to chain calls
     */
    public Level addEnemies(final Mob... enemies) {
        for (final Mob mob : enemies) {
            this.enemies.add(mob);
        }
        return this;
    }

    /**
     * Sets the position of the door to the specified one.
     * 
     * @param doorPos new position of the door
     */
    public void setDoorPosition(final Position doorPos) {
        this.doorPosition.setPosition(doorPos);
    }

    /**
     * Removes an item from the level.
     * 
     * @param item
     */
    public void removeItem(final Item item) {
        items.remove(item);
    }

    /**
     * Removes an enemy item from the level.
     * 
     * @param enemy
     */
    public void removeMob(final Mob enemy) {
        enemies.remove(enemy);
    }

    /**
     * Removes dead mobs from the level and saves their position.
     */
    public void removeDeadMobs() {
        enemies.stream().filter(m -> m.isDead()).forEach(m -> this.deadMobPositions.add(m.getPos()));
        enemies.removeIf(m -> m.isDead());
    }

    /**
     * @return a list containing all the items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * @return a list containing all the consumables
     */
    public List<ConsumableItem> getConsumables() {
        final List<ConsumableItem> res = new LinkedList<>();
        items.stream().filter(i -> i instanceof ConsumableItem).forEach(item -> res.add((ConsumableItem) item));
        return res;
    }

    /**
     * @return a list containing all the weapons
     */
    public List<Weapon> getWeapons() {
        final List<Weapon> res = new LinkedList<>();
        items.stream().filter(i -> i instanceof Weapon).forEach(item -> res.add((Weapon) item));
        return res;
    }

    /**
     * @return a list containing all the enemies
     */
    public List<Mob> getEnemies() {
        return new LinkedList<>(enemies);
    }

    /**
     * @return a list containing the position of dead enemies
     */
    public List<Position> getDeadMobPositions() {
        return deadMobPositions;
    }

    /**
     * @return the ending level door position
     */
    public Position getDoorPosition() {
        return doorPosition;
    }

    /**
     * Gets the map and its unitScale.
     * 
     * @return a pair containing the map and the unitScale (float)
     */
    public Pair<DescentMap, Float> getMap() {
        return this.map;
    }

    /**
     * @return a string with all the content of the level
     */
    public String toString() {
        int i = 0;
        String msg = "\nLEVEL: \n";
        if (!enemies.isEmpty()) {
            msg += "Enemies:";
            for (final Mob e : enemies) {
                msg += "\n" + ++i + ": [" + e.toString() + "]\nPosition: " + e.getPos().toString();
            }
        } else {
            msg += "No Enemies";
        }
        i = 0;
        if (!items.isEmpty()) {
            msg += "\nItems:";
            for (final Item item : items) {
                msg += "\n" + ++i + ": [ " + item.getName() + " ], Position: " + item.getPos().toString();
            }
        } else {
            msg += "\nNo Items";
        }
        return msg;
    }
    
}
