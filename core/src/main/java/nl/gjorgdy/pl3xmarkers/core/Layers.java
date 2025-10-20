package nl.gjorgdy.pl3xmarkers.core;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.layers.EndGatewayIconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.AreaMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.EndPortalIconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.IconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.NetherPortalIconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import org.intellij.lang.annotations.Language;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class Layers {

    public static class Keys {
        public static String OPAC = "openpartiesandclaims";
        public static String BEACONS = "beacons";
        public static String END_GATEWAYS = "end_gateways";
        public static String END_PORTALS = "end_portals";
        public static String NETHER_PORTALS = "nether_portals";
        public static String AREAS = "areas";
    }

    public static class Labels {
        public static String OPAC = "Open Parties and Claims";
        public static String BEACONS = "Beacons";
        public static String END_GATEWAYS = "End Gateways";
        public static String END_PORTALS = "End Portals";
        public static String NETHER_PORTALS = "Nether Portals";
        public static String AREAS = "Areas";
    }

    public static class Tooltips {
        @Language("HTML")
        public static String OPAC = "Open Parties and Claims";
        @Language("HTML")
        public static String BEACONS = "Beacon";
        @Language("HTML")
        public static String END_GATEWAYS = "End Gateway";
        @Language("HTML")
        public static String END_PORTALS = "End Portal";
        @Language("HTML")
        public static String NETHER_PORTALS = "Nether Portal";
    }

    public static Set<Function<World, MarkerLayer>> ALL = Set.of(
		OPACAreaMarkerLayer::new,
        w -> new IconMarkerLayer(Icons.Keys.BEACON, Keys.BEACONS, Labels.BEACONS, Tooltips.BEACONS, w),
        EndGatewayIconMarkerLayer::new,
        EndPortalIconMarkerLayer::new,
        NetherPortalIconMarkerLayer::new,
        w -> new AreaMarkerLayer(Keys.AREAS, Labels.AREAS, w)
    );

}
