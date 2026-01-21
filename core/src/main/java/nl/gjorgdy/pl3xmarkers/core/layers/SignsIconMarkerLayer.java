package nl.gjorgdy.pl3xmarkers.core.layers;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISignMarker;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.intellij.lang.annotations.Language;

public class SignsIconMarkerLayer extends MarkerLayer {

	public final String key = Layers.Keys.SIGNS;
	public final String iconId = Icons.Keys.SIGN;

	public SignsIconMarkerLayer(World world, int priority) {
		super(Layers.Keys.SIGNS, Layers.Labels.SIGNS, world, priority);
	}

	@Override
	public void load() {
		Pl3xMarkersCore.storage()
				.getSignMarkerRepository()
				.getMarkers(getWorld().getKey(), key)
				.forEach(this::addMarker);
	}

	/**
	 * Rename a marker
	 *
	 * @param x    x coordinate of marker
	 * @param z    z coordinate of marker
	 * @param text new name of the marker
	 */
	public boolean editMarker(int x, int z, @Language("HTML") String[] text) {
		if (text == null || text.length != 4) {
			throw new IllegalArgumentException("text should be a String array with a size of 4");
		}
		return Pl3xMarkersCore.storage()
					   .getSignMarkerRepository()
					   .editMarker(getWorld().getKey(), key, x, z, text);
	}

	/**
	 * Add and store a new marker
	 *
	 * @param x    x coordinate of marker
	 * @param z    z coordinate of marker
	 * @param text name of the marker
	 */
	public boolean createMarker(int x, int z, @Language("HTML") String[] text) {
		if (text == null || text.length != 4) {
			throw new IllegalArgumentException("text should be a String array with a size of 4");
		}
		var marker = Pl3xMarkersCore.storage()
							 .getSignMarkerRepository()
							 .createMarker(getWorld().getKey(), key, x, z, text);
		if (marker != null) {
			addMarker(marker);
			return true;
		}
		return false;
	}

	/**
	 * Remove a marker
	 *
	 * @param x x coordinate of marker
	 * @param z z coordinate of marker
	 */
	public boolean removeMarker(int x, int z) {
		var removed = Pl3xMarkersCore.storage()
							  .getSignMarkerRepository()
							  .removeMarker(getWorld().getKey(), key, x, z);
		if (removed) {
			super.removeMarker(toMarkerKey(x, z));
		}
		return removed;
	}

	protected <T extends ISignMarker> void addMarker(T markerEntity) {
		if (markerEntity.getText() == null || markerEntity.getText().length > 4) {
			throw new IllegalArgumentException("text should be a String array with a size of 4");
		}
		var sanitizedText = new String[4];
		for (int i = 0; i < markerEntity.getText().length; i++) {
			sanitizedText[i] = HtmlHelper.sanitize(markerEntity.getText()[i]);
		}
		// Create tooltip by joining lines with <br> after sanitizing to prevent HTML injection
		@Language("HTML") var tooltip = String.join("<br>", sanitizedText);
		var marker = IconMarkerBuilder.newIconMarker(
						markerEntity.getKey(),
						iconId,
						markerEntity.getLocation().getX(),
						markerEntity.getLocation().getZ()
				)
							 .centerIcon(16, 16)
							 .addTooltip(tooltip)
							 .build();
		addMarker(marker);
	}

}
