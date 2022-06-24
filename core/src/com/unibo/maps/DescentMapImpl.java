package com.unibo.maps;

import java.util.Optional;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.unibo.game.Descent;
import com.unibo.util.Position;
import com.unibo.view.CharacterView;

/**
 * Implementation of the map interface that leverages the libgdx and Tiledmap framework.
 */
public class DescentMapImpl implements DescentMap {

    private final MapLayer collisionLayer;
    private final MapLayer teleportLayer;
    private final MapLayer specialTilesLayer;
    private final TiledMap map;
    private final Position startingPosition;
    private final float unitScale;
    private String backgroundMusic;
    private static final float VOLUME = (float) 1.0;

    /**
     * Constructor for a map.
     * 
     * @param path        of the map
     * @param startingPos of the hero
     * @param unitScale   float value to transform world units to pixel
     * @param isFileExt   true if the file is external
     * @param musicPath   the background music's file path
     */
    public DescentMapImpl(final String path, final Position startingPos, final float unitScale, final Boolean isFileExt, final String musicPath) {
        if (isFileExt) {
            this.map = new TmxMapLoader(new AbsoluteFileHandleResolver()).load(Descent.CUSTOM_LEVELS_PATH + path);
        } else {
            this.map = new TmxMapLoader().load(path);
        }
        this.unitScale = unitScale;
        this.collisionLayer = this.map.getLayers().get("objects");
        this.teleportLayer = this.map.getLayers().get("teleports");
        this.specialTilesLayer = this.map.getLayers().get("special");
        this.startingPosition = startingPos;
        this.backgroundMusic = musicPath;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean validMovement(final CharacterView charView, final float newX, final float newY) {
        return polyScanner(charView, new Position(newX, newY), collisionLayer, TileAction.COLLISION);
    }

    @Override
    public Boolean checkTeleport(final CharacterView charView) {
        return teleportLayer == null ? false
                : polyScanner(charView, new Position(charView.getCharacter().getPos().getxCoord(),
                        charView.getCharacter().getPos().getyCoord()), teleportLayer, TileAction.TELEPORT);
    }

    @Override
    public Boolean checkDamageTile(final CharacterView charView) {
        return specialTilesLayer == null ? false
                : polyScanner(charView, new Position(charView.getCharacter().getPos().getxCoord(),
                        charView.getCharacter().getPos().getyCoord()), specialTilesLayer, TileAction.DAMAGE);
    }

    private Boolean polyScanner(final CharacterView charView, final Position pos, final MapLayer layer,
            final TileAction action) {
        Polygon poly = getProjectedCharacterPolygon(charView, pos);
        for (final PolygonMapObject polyMapObj : layer.getObjects().getByType(PolygonMapObject.class)) {
            final Polygon polyObj = new Polygon(polyMapObj.getPolygon().getTransformedVertices());
            var verts = polyObj.getTransformedVertices();
            for (int i = 0; i < verts.length; i++) {
                verts[i] *= unitScale;
            }
            if (Intersector.overlapConvexPolygons(poly, polyObj)) {
                switch (action) {
                case COLLISION:
                    return false;
                case TELEPORT:
                    charView.getCharacter().setPos((int) ((int) polyMapObj.getProperties().get("X") * unitScale),
                            (int) ((int) polyMapObj.getProperties().get("Y") * unitScale));
                    return true;
                case DAMAGE:
                    return true;
                default:
                    break;
                }
            }
        }
        switch (action) {
        case COLLISION:
            return true;
        case DAMAGE:
            return false;
        case TELEPORT:
            return false;
        default:
            return true;
        }
    }

    @Override
    public TiledMapTileLayer getLayer(final int layerNumber) {
        return (TiledMapTileLayer) map.getLayers().get(layerNumber);
    }

    @Override
    public TiledMapTileLayer getLayer(final String path) {
        return (TiledMapTileLayer) map.getLayers().get(path);
    }

    @Override
    public MapLayer getCollisionLayer() {
        return collisionLayer;
    }

    @Override
    public Optional<MapLayer> getTeleportLayer() {
        return Optional.ofNullable(teleportLayer);
    }

    @Override
    public Optional<MapLayer> getSpecialTilesLayer() {
        return Optional.ofNullable(specialTilesLayer);
    }

    @Override
    public Position getStartingPosition() {
        return startingPosition;
    }

    @Override
    public TiledMap getTiledMap() {
        return this.map;
    }
   
    @Override
    public Float getUnitScale() {
    	return this.unitScale;
    
    private Polygon getProjectedCharacterPolygon(final CharacterView character, final Position pos) {
    	final Rectangle rect = character.getCharRect();
        rect.setPosition(pos.getxCoord() - rect.getWidth() / 2, pos.getyCoord());
        Polygon poly = new Polygon(new float[] { rect.x, rect.y, rect.x + rect.width, rect.y, rect.x + rect.width,
                rect.y + rect.height, rect.x, rect.y + rect.height });
        return poly;
    }

    /**
   	 * {@inheritDoc}
   	 */
	public String getBackgroundSong() {
		return backgroundMusic;
	}

	 /**
		 * {@inheritDoc}
		 */
	public void setBackgroundSong(final String path) {
		this.backgroundMusic = path;
	}

}
