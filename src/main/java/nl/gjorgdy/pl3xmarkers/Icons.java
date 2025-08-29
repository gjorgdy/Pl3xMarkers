package nl.gjorgdy.pl3xmarkers;

public class Icons {

    public static class Keys {
        public static final String BEACON = "beacon";
        public static final String END_GATEWAY = "end_gateway";
        public static final String END_PORTAL = "end_portal";
        public static final String NETHER_PORTAL = "nether_portal";
    }

    public static void register(Api api) {
        api.registerIconImage("/assets/pl3xmarkers/icons/", Keys.BEACON, "png");
        api.registerIconImage("/assets/pl3xmarkers/icons/", Keys.END_GATEWAY, "png");
        api.registerIconImage("/assets/pl3xmarkers/icons/", Keys.END_PORTAL, "png");
        api.registerIconImage("/assets/pl3xmarkers/icons/", Keys.NETHER_PORTAL, "png");
    }

}
