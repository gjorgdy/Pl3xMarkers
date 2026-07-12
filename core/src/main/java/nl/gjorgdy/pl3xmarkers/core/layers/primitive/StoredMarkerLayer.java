package nl.gjorgdy.pl3xmarkers.core.layers.primitive;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.interfaces.IMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.IMarker;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class StoredMarkerLayer<T extends IMarker, R extends IMarkerRepository<? extends T>> extends MarkerLayer<T> {

	public final String worldIdentifier;

	public StoredMarkerLayer(String key, String label, @NotNull World world, int priority) {
		super(key, label, world, priority);
		worldIdentifier = world.getKey();
		setPriority(priority);
	}

	public abstract Optional<? extends T> getMarker(String key);

	public abstract Optional<String> getClosestMarker(int x, int y, int z);

	public void addMarker(MarkerBuilder<?> markerBuilder) {
		addMarker(markerBuilder.build());
	}

	public void updateMarker(T markerEntity) {
		removeMarker(markerEntity);
		loadMarker(markerEntity);
	}

	public void removeMarker(T markerEntity) {
		super.removeMarker(markerEntity.getKey());
	}

	public boolean hasMarker(T markerEntity) {
		return super.hasMarker(markerEntity.getKey());
	}

	protected abstract R getRepository();
}
