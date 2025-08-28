package eu.hexasis.helixmarkers;

import eu.hexasis.helixmarkers.layers.primitive.AreaMarkerLayer;
import eu.hexasis.helixmarkers.layers.EndPortalIconMarkerLayer;
import eu.hexasis.helixmarkers.layers.primitive.IconMarkerLayer;
import eu.hexasis.helixmarkers.layers.NetherPortalIconMarkerLayer;
import org.intellij.lang.annotations.Language;

public class Layers {

    public static class Keys {
        public static String BEACONS = "beacons";
        public static String END_GATEWAYS = "end_gateways";
        public static String END_PORTALS = "end_portals";
        public static String NETHER_PORTALS = "nether_portals";
        public static String AREAS = "areas";
    }

    public static class Labels {
        public static String BEACONS = "Beacons";
        public static String END_GATEWAYS = "End Gateways";
        public static String END_PORTALS = "End Portals";
        public static String NETHER_PORTALS = "Nether Portals";
        public static String AREAS = "Areas";
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

    public static void register(Api api) {
        api.registerMarkerLayer(w -> new IconMarkerLayer(Icons.Keys.BEACON, Keys.BEACONS, Labels.BEACONS, Tooltips.BEACONS, w));
        api.registerMarkerLayer(w -> new IconMarkerLayer(Icons.Keys.END_GATEWAY, Keys.END_GATEWAYS, Labels.END_GATEWAYS, Tooltips.END_GATEWAYS, w));
        api.registerMarkerLayer(EndPortalIconMarkerLayer::new);
        api.registerMarkerLayer(NetherPortalIconMarkerLayer::new);
        api.registerMarkerLayer(w -> new AreaMarkerLayer(Keys.AREAS, Labels.AREAS, w));
    }

}
