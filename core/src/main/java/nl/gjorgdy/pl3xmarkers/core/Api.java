package nl.gjorgdy.pl3xmarkers.core;

import net.pl3x.map.core.Pl3xMap;
import net.pl3x.map.core.markers.layer.Layer;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.layers.SignsIconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.AreaMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.IconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.Boundary;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.objects.LayerFactory;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.intellij.lang.annotations.Language;

import java.util.Optional;

public class Api {
    @SuppressWarnings("unused")
    private static World getWorld(String worldIdentifier) {
        World world = Pl3xMap.api().getWorldRegistry().get(worldIdentifier);
		if (world == null) {
			throw new RuntimeException("World not found " + worldIdentifier);
		}
        return world;
    }

	@SuppressWarnings("unused")
	public Optional<Boundary> getAreaBoundary(String worldIdentifier, int x, int z) {
		World world = Pl3xMap.api().getWorldRegistry().get(worldIdentifier);
		if (world == null) {
			return Optional.empty();
		}
		var layer = getWorld(worldIdentifier).getLayerRegistry().get(Layers.Keys.AREAS);
		if (layer instanceof AreaMarkerLayer aml) {
			return aml.getAreaBoundary(x, z);
		}
		return Optional.empty();
	}

    @SuppressWarnings("unused")
    public void registerMarkerLayer(LayerFactory factory) {
        // parse through to internal handler
        Pl3xMarkersCore.pl3xHandler().registerMarkerLayer(factory);
    }

    @SuppressWarnings("unused")
    public void registerIconImage(String path, String filename, String filetype) {
        // parse through to internal handler
        Pl3xMarkersCore.pl3xHandler().registerIconImage(path, filename, filetype);
    }

    @SuppressWarnings("unused")
    public InteractionResult addAreaPoint(String worldIdentifier, @Language("HTML") String label, int color, int x, int z) {
		Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(Layers.Keys.AREAS);
		if (layer instanceof AreaMarkerLayer aml) {
			var added = aml.addPoint(label, color, x, z);
			return added ? new InteractionResult(InteractionResult.State.ADDED, "Added a point to area: " + label) :
						   InteractionResult.skip();
		}
		return new InteractionResult(InteractionResult.State.FAILURE, "Could not add point to area: " + label);
    }

    @SuppressWarnings("unused")
    public InteractionResult removeAreaPoint(String worldIdentifier, @Language("HTML") String label, int color, int x, int z) {
		Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(Layers.Keys.AREAS);
		if (layer instanceof AreaMarkerLayer aml) {
			var removed = aml.removePoint(label, color, x, z);
			return removed ? new InteractionResult(InteractionResult.State.REMOVED, "Removed a point from area: " + label) :
			   InteractionResult.skip();
		}
		return new InteractionResult(InteractionResult.State.FAILURE, "Could not remove point from area: " + label);
    }

	public InteractionResult editSignMarker(String worldIdentifier, int x, int z, @Language("HTML") String[] text) {
		Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(Layers.Keys.SIGNS);
		if (layer instanceof SignsIconMarkerLayer signsLayer) {
			var edited = signsLayer.editMarker(x, z, text);
			return edited ? new InteractionResult(InteractionResult.State.ADDED, "Sign marker edited") :
						   new InteractionResult(InteractionResult.State.ADDED, "Sign marker added");
		}
		return new InteractionResult(InteractionResult.State.FAILURE, "Could not edit sign marker");
	}

	public InteractionResult addSignMarker(String worldIdentifier, int x, int z, @Language("HTML") String[] text) {
		Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(Layers.Keys.SIGNS);
		if (layer instanceof SignsIconMarkerLayer signsLayer) {
			var added = signsLayer.createMarker(x, z, text);
			return added ? new InteractionResult(InteractionResult.State.ADDED, "Sign marker added") :
						   InteractionResult.skip();
		}
		return new InteractionResult(InteractionResult.State.FAILURE, "Could not add sign marker");
	}

	public InteractionResult removeSignMarker(String worldIdentifier, int x, int z) {
		Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(Layers.Keys.SIGNS);
		if (layer instanceof SignsIconMarkerLayer signsLayer) {
			var removed = signsLayer.removeMarker(x, z);
			return removed ? new InteractionResult(InteractionResult.State.REMOVED, "Sign marker removed") :
						   InteractionResult.skip();
		}
		return new InteractionResult(InteractionResult.State.FAILURE, "Could not remove sign marker");
	}

    public InteractionResult addNetherPortalIconMarker(String worldIdentifier, int x, int z) {
        var added = addIconMarker(worldIdentifier, Layers.Keys.NETHER_PORTALS, x, z);
		return added ? new InteractionResult(InteractionResult.State.ADDED, "Nether Portal marker added") :
		   InteractionResult.skip();
    }

    public InteractionResult addEndGatewayIconMarker(String worldIdentifier, int x, int z) {
        var added = addIconMarker(worldIdentifier, Layers.Keys.END_GATEWAYS, x, z);
		return added ? new InteractionResult(InteractionResult.State.ADDED, "End Gateway marker added") :
		   InteractionResult.skip();
    }

    public InteractionResult addEndPortalIconMarker(String worldIdentifier, int x, int z) {
		var added = addIconMarker(worldIdentifier, Layers.Keys.END_PORTALS, x, z);
		return added ? new InteractionResult(InteractionResult.State.ADDED, "End Portal marker added") :
		   InteractionResult.skip();
    }

    public InteractionResult addBeaconIconMarker(String worldIdentifier, int x, int z) {
        var added = addIconMarker(worldIdentifier, Layers.Keys.BEACONS, x, z);
		return added ? new InteractionResult(InteractionResult.State.ADDED, "Beacon marker added") :
		   InteractionResult.skip();
    }

    private boolean addIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
		if (layer instanceof IconMarkerLayer simpleIconMarkerLayer) {
			return simpleIconMarkerLayer.addSimpleMarker(x, z);
		}
		return false;
    }

    public InteractionResult removeNetherPortalIconMarker(String worldIdentifier, int x, int z) {
        var removed = removeIconMarker(worldIdentifier, Layers.Keys.NETHER_PORTALS, x, z);
		return removed ? new InteractionResult(InteractionResult.State.REMOVED, "Nether Portal marker removed") :
		   InteractionResult.skip();
    }

    @SuppressWarnings("unused")
    public InteractionResult removeEndGatewayIconMarker(String worldIdentifier, int x, int z) {
		var removed = removeIconMarker(worldIdentifier, Layers.Keys.END_GATEWAYS, x, z);
		return removed ? new InteractionResult(InteractionResult.State.REMOVED, "End Gateway marker removed") :
		   InteractionResult.skip();
    }

    @SuppressWarnings("unused")
    public InteractionResult removeEndPortalIconMarker(String worldIdentifier, int x, int z) {
		var removed = removeIconMarker(worldIdentifier, Layers.Keys.END_PORTALS, x, z);
		return removed ? new InteractionResult(InteractionResult.State.REMOVED, "End Portal marker removed") :
			InteractionResult.skip();
    }

    public InteractionResult removeBeaconIconMarker(String worldIdentifier, int x, int z) {
        var removed = removeIconMarker(worldIdentifier, Layers.Keys.BEACONS, x, z);
		return removed ? new InteractionResult(InteractionResult.State.REMOVED, "Beacon marker removed") :
		   InteractionResult.skip();
    }

    private boolean removeIconMarker(String worldIdentifier, String layerKey, int x, int z) {
		Layer layer = getWorld(worldIdentifier).getLayerRegistry().get(layerKey);
		if (layer instanceof IconMarkerLayer markerLayer) {
			return markerLayer.removeMarker(x, z);
		}
		return false;
    }

}
