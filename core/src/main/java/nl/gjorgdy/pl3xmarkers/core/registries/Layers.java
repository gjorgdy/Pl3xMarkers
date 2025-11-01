package nl.gjorgdy.pl3xmarkers.core.registries;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.objects.LayerFactory;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.helpers.WorldHelpers;
import nl.gjorgdy.pl3xmarkers.core.layers.EndGatewayIconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.AreaMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.EndPortalIconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.IconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.NetherPortalIconMarkerLayer;
import org.intellij.lang.annotations.Language;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class Layers {

    public static class Keys {
        public static String BEACONS = "beacons";
        public static String END_GATEWAYS = "end_gateways";
        public static String END_PORTALS = "end_portals";
        public static String NETHER_PORTALS = "nether_portals";
        public static String AREAS = "areas";
		public static String OPAC = "open_parties_and_claims";
    }

    public static class Labels {
        public static String BEACONS = "Beacons";
        public static String END_GATEWAYS = "End Gateways";
        public static String END_PORTALS = "End Portals";
        public static String NETHER_PORTALS = "Nether Portals";
        public static String AREAS = "Areas";
		public static String OPAC = "Open Parties and Claims";
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

	private static final Set<LayerFactory> ALL = new HashSet<>(Set.of(
			new LayerFactory(
					world -> new IconMarkerLayer(Icons.Keys.BEACON, Keys.BEACONS, Labels.BEACONS, Tooltips.BEACONS, world, MarkersConfig.BEACON_MARKERS_PRIORITY),
					world -> MarkersConfig.BEACON_MARKERS_ENABLED
			),
			new LayerFactory(
					EndGatewayIconMarkerLayer::new,
					world -> MarkersConfig.END_GATEWAY_MARKERS_ENABLED && WorldHelpers.isEnd(world)
			),
			new LayerFactory(
					EndPortalIconMarkerLayer::new,
					world -> MarkersConfig.END_PORTAL_MARKERS_ENABLED && WorldHelpers.isOverworld(world)
			),
			new LayerFactory(
					NetherPortalIconMarkerLayer::new,
					world -> MarkersConfig.NETHER_PORTAL_MARKERS_ENABLED && (WorldHelpers.isOverworld(world) || WorldHelpers.isNether(world))
			),
			new LayerFactory(
					world -> new AreaMarkerLayer(Keys.AREAS, Labels.AREAS, world, MarkersConfig.AREA_MARKERS_PRIORITY),
					world -> MarkersConfig.AREA_MARKERS_ENABLED
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

}
