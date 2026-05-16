package nl.gjorgdy.pl3xmarkers.core.layers;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.interfaces.ISignMarkerRepository;
import nl.gjorgdy.pl3xmarkers.core.interfaces.entities.ISignMarker;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.objects.InteractionResult;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.intellij.lang.annotations.Language;

import java.util.ArrayList;
import java.util.List;

public class SignsMarkerLayer extends MarkerLayer {

	public final String key = Layers.Keys.SIGNS;
	public final String iconId = Icons.Keys.SIGN;

	public SignsMarkerLayer(World world) {
		super(Layers.Keys.SIGNS, Layers.Labels.SIGNS, world, MarkersConfig.SIGN_MARKERS_PRIORITY);
	}

	@Override
	public void load() {
		getRepository().foreach(this::addMarker);
	}

	/**
	 * Set the text of a sign marker
	 *
	 * @param x    x coordinate of marker
	 * @param y    y coordinate of marker
	 * @param z    z coordinate of marker
	 * @param text new text of the marker
	 */
	public InteractionResult set(int x, int y, int z, @Language("HTML") String[] text) {
		if (text == null || text.length != 4) {
			return InteractionResult.failure("Text should be a String array with a size of 4");
		}
		boolean edited = false;
		if (hasMarker(toMarkerKey(x, y, z))) {
			super.removeMarker(toMarkerKey(x, y, z));
			edited = true;
		}
		addMarker(
				getRepository().editOrCreate(x, y, z, text)
		);
		return InteractionResult.added(edited ? "Edited sign marker" : "Added sign marker");
	}

	/**
	 * Remove a marker
	 *
	 * @param x x coordinate of marker
	 * @param y y coordinate of marker
	 * @param z z coordinate of marker
	 */
	public InteractionResult remove(int x, int y, int z) {
		var removed = getRepository().remove(x, y, z);
		if (removed) {
			super.removeMarker(toMarkerKey(x, y, z));
			return InteractionResult.removed("Removed sign marker");
		}
		return InteractionResult.skip();
	}

	protected <T extends ISignMarker> void addMarker(T markerEntity) {
		List<String> sanitizedText = new ArrayList<>();
		var text = markerEntity.getText();
		if (MarkersConfig.SIGN_MARKERS_FILL_LINES) {
			for (int i = 0; i < 4; i++) {
				if (i < text.length) {
					sanitizedText.add(HtmlHelper.sanitize(text[i]));
				}
			}
		} else {
			for (@Language("HTML") String line : text) {
				if (line.isBlank()) {
					continue;
				}
				sanitizedText.add(HtmlHelper.sanitize(line));
			}
		}
		// Create tooltip by joining lines with <br> after sanitizing to prevent HTML injection
		@Language("HTML") var tooltip = String.join("<br>", sanitizedText);
		var markerBuilder = IconMarkerBuilder.newIconMarker(
						markerEntity.getKey(),
						iconId,
						markerEntity.getPosition().x(),
						markerEntity.getPosition().z()
				)
				.centerIcon(16, 16);
		if (MarkersConfig.SIGN_MARKERS_ALWAYS_SHOW) {
			markerBuilder.addPermanentBottomTooltip(tooltip);
		} else {
			markerBuilder.addTooltip(tooltip);
		}
		addMarker(markerBuilder.build());
	}

	private ISignMarkerRepository<?> getRepository() {
		return Pl3xMarkersCore.storage()
				.getWorldRepository(worldIdentifier)
				.getSignMarkerRepository(getKey());
	}

}
