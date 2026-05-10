package nl.gjorgdy.pl3xmarkers.core.layers;

import net.pl3x.map.core.configuration.PlayersLayerConfig;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LightningMarkerLayer extends MarkerLayer {

	private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

	public LightningMarkerLayer(@NonNull World world) {
		super(Layers.Keys.LIGHTNING, Layers.Labels.LIGHTNING, world, MarkersConfig.LIGHTNING_MARKERS_PRIORITY);
		setUpdateInterval(PlayersLayerConfig.UPDATE_INTERVAL);
		setLiveUpdate(PlayersLayerConfig.LIVE_UPDATE);
	}

	@Override
	public void load() {
		// ignore
	}

	public void show(int x, int y, int z) {
		var marker = createIconMarker(x, y, z);
		addMarker(marker);
		executorService.schedule(
				() -> removeMarker(marker.getKey()),
				MarkersConfig.LIGHTNING_MARKERS_LIFETIME,
				TimeUnit.SECONDS
		);
	}

	private Marker<?> createIconMarker(int x, int y, int z) {
		return IconMarkerBuilder.newIconMarker(
						toMarkerKey(x, y, z),
						Icons.Keys.LIGHTNING,
						x, z
				)
				.centerIcon(16, 16)
				.build();
	}

}
