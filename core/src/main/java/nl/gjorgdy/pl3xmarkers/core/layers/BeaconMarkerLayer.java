package nl.gjorgdy.pl3xmarkers.core.layers;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.SimpleMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.jetbrains.annotations.NotNull;

public class BeaconMarkerLayer extends SimpleMarkerLayer {

	public BeaconMarkerLayer(@NotNull World world) {
		super(Icons.Keys.BEACON, Layers.Keys.BEACONS, Layers.Labels.BEACONS, Layers.Tooltips.BEACONS, world,
		      MarkersConfig.BEACON_MARKERS_PRIORITY
		);
	}
}