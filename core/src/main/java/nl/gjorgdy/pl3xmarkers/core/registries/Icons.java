package nl.gjorgdy.pl3xmarkers.core.registries;

import nl.gjorgdy.pl3xmarkers.core.objects.IconImageAddress;

import java.util.Set;

public class Icons {

    public static Set<IconImageAddress> ALL = Set.of(
            new IconImageAddress("/assets/pl3xmarkers/icons/", Keys.BEACON, "png"),
            new IconImageAddress("/assets/pl3xmarkers/icons/", Keys.END_GATEWAY, "png"),
            new IconImageAddress("/assets/pl3xmarkers/icons/", Keys.END_PORTAL, "png"),
            new IconImageAddress("/assets/pl3xmarkers/icons/", Keys.NETHER_PORTAL, "png"),
            new IconImageAddress("/assets/pl3xmarkers/icons/", Keys.SIGN, "png"),
            new IconImageAddress("/assets/pl3xmarkers/icons/", Keys.SHOPKEEPERS, "png")
    );

    public static class Keys {
        public static final String BEACON = "beacon";
        public static final String END_GATEWAY = "end_gateway";
        public static final String END_PORTAL = "end_portal";
        public static final String NETHER_PORTAL = "nether_portal";
        public static final String SIGN = "sign";
        public static final String SHOPKEEPERS = "shop";
    }

}
