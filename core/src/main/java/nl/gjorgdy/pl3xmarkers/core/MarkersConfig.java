package nl.gjorgdy.pl3xmarkers.core;

import net.pl3x.map.core.configuration.AbstractConfig;

public class MarkersConfig extends AbstractConfig {

	private static final MarkersConfig CONFIG = new MarkersConfig();

	public static void reload() {
		CONFIG.reload(Pl3xMarkersCore.getMainDir().resolve("config.yml"), MarkersConfig.class);
	}

	@Key("settings.feedback.messages")
	@Comment("Enable action messages when adding/removing markers or points")
	public static boolean FEEDBACK_MESSAGES_ENABLED = true;

	@Key("settings.feedback.sound")
	@Comment("Enable sound effects when adding/removing markers or points")
	public static boolean FEEDBACK_SOUNDS_ENABLED = true;

	@Key("marker-settings.areas.enabled")
	@Comment("Enable player made areas on the map")
	public static boolean AREA_MARKERS_ENABLED = true;

	@Key("marker-settings.areas.size")
	@Comment("The maximum size that an area is allowed to be \nthis is the distance between the two furthest points in the area")
	public static int AREA_MARKERS_MAX_SIZE = 512;

	@Key("marker-settings.nether_portals.enabled")
	@Comment("Enable nether portal markers on the map")
	public static boolean NETHER_PORTAL_MARKERS_ENABLED = true;

	@Key("marker-settings.beacons.enabled")
	@Comment("Enable beacon markers on the map")
	public static boolean BEACON_MARKERS_ENABLED = true;

	@Key("marker-settings.end_portals.enabled")
	@Comment("Enable end portal markers on the map")
	public static boolean END_PORTAL_MARKERS_ENABLED = true;

	@Key("marker-settings.end_gateways.enabled")
	@Comment("Enable end gateway markers on the map")
	public static boolean END_GATEWAY_MARKERS_ENABLED = true;
}
