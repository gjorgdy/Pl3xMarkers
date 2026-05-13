package nl.gjorgdy.pl3xmarkers.core.registries;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.helpers.WorldHelpers;
import nl.gjorgdy.pl3xmarkers.core.layers.*;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.AreaMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.LayerFactory;
import org.intellij.lang.annotations.Language;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class Layers {

	private static final Set<LayerFactory> ALL = new HashSet<>(Set.of(
			new LayerFactory(
					BeaconMarkerLayer::new,
					_ -> MarkersConfig.BEACON_MARKERS_ENABLED
			),
			new LayerFactory(
					EndGatewayMarkerLayer::new,
					world -> MarkersConfig.END_GATEWAY_MARKERS_ENABLED && WorldHelpers.isEnd(world)
			),
			new LayerFactory(
					EndPortalMarkerLayer::new,
					world -> MarkersConfig.END_PORTAL_MARKERS_ENABLED && WorldHelpers.isOverworld(world)
			),
			new LayerFactory(
					NetherPortalMarkerLayer::new,
					world -> MarkersConfig.NETHER_PORTAL_MARKERS_ENABLED && (WorldHelpers.isOverworld(world) || WorldHelpers.isNether(world))
			),
			new LayerFactory(
					AreaMarkerLayer::new,
					_ -> MarkersConfig.AREA_MARKERS_ENABLED
			),
			new LayerFactory(
					SignsMarkerLayer::new,
					_ -> MarkersConfig.SIGN_MARKERS_ENABLED
			),
			new LayerFactory(
					LightningMarkerLayer::new,
					world -> MarkersConfig.LIGHTNING_MARKERS_ENABLED && WorldHelpers.isOverworld(world)
			)
	));

	public static void register(Function<World, MarkerLayer> builder, Predicate<World> predicate) {
		register(new LayerFactory(builder, predicate));
	}

	public static void register(LayerFactory factory) {
		ALL.add(factory);
	}

	public static Set<LayerFactory> getAll() {
		return ALL;
	}

	public static class Keys {
		public static String BEACONS = "beacons";
		public static String END_GATEWAYS = "end_gateways";
		public static String END_PORTALS = "end_portals";
		public static String NETHER_PORTALS = "nether_portals";
		public static String AREAS = "areas";
		public static String OPAC = "open_parties_and_claims";
		public static String SIGNS = "signs";
		public static String SHOPKEEPERS = "shopkeepers";
		public static String LIGHTNING = "lightning";
	}

	public static class Labels {
		public static String BEACONS = "Beacons";
		public static String END_GATEWAYS = "End Gateways";
		public static String END_PORTALS = "End Portals";
		public static String NETHER_PORTALS = "Nether Portals";
		public static String AREAS = "Areas";
		public static String OPAC = "Open Parties and Claims";
		public static String SIGNS = "Signs";
		public static String SHOPKEEPERS = "Shopkeepers";
		public static String LIGHTNING = "Lightning Strikes";
	}

	public static class Tooltips {
		@Language("HTML")
		public static String BEACONS = "Beacon";
		@Language("HTML")
		public static String END_GATEWAYS = "End Gateway";
		@Language("HTML")
		public static String END_PORTALS = "End Portal";
		@Language("HTML")
		public static String NETHER_PORTALS = "Nether Portal";
	}

}
