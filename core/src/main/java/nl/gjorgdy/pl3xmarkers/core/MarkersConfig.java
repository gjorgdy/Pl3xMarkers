package nl.gjorgdy.pl3xmarkers.core;

import net.pl3x.map.core.configuration.AbstractConfig;

public class MarkersConfig extends AbstractConfig {

	private static final MarkersConfig CONFIG = new MarkersConfig();
	@Key("settings.feedback.messages")
	@Comment("Enable action messages when adding/removing markers or points")
	public static boolean FEEDBACK_MESSAGES_ENABLED = true;
	@Key("settings.feedback.sound")
	@Comment("Enable sound effects when adding/removing markers or points")
	public static boolean FEEDBACK_SOUNDS_ENABLED = true;
	@Key("settings.feedback.area-enter")
	@Comment("Enable action messages when entering an area")
	public static boolean FEEDBACK_AREA_ENTER_ENABLED = true;
	@Key("messages.marker.add")
	@Comment("Message shown when adding a marker. Use {type} for the marker type")
	public static String MESSAGE_MARKER_ADD = "Added {type} marker";
	@Key("messages.marker.rename")
	@Comment("Message shown when renaming a marker. Use {type} and {name}")
	public static String MESSAGE_MARKER_RENAME = "Renamed {type} marker to '{name}'";
	@Key("messages.marker.rename-failed")
	@Comment("Message shown when renaming a marker fails. Use {type}")
	public static String MESSAGE_MARKER_RENAME_FAILED = "Could not rename {type} marker";
	@Key("messages.marker.color")
	@Comment("Message shown when coloring a marker. Use {type}")
	public static String MESSAGE_MARKER_COLOR = "Colored {type} marker";
	@Key("messages.marker.color-failed")
	@Comment("Message shown when coloring a marker fails. Use {type}")
	public static String MESSAGE_MARKER_COLOR_FAILED = "Could not color {type} marker";
	@Key("messages.marker.remove")
	@Comment("Message shown when removing a marker. Use {type}")
	public static String MESSAGE_MARKER_REMOVE = "Removed {type} marker";
	@Key("messages.area.create")
	@Comment("Message shown when creating an area. Use {label} for its name")
	public static String MESSAGE_AREA_CREATE = "Created area: {label}";
	@Key("messages.area.point-add")
	@Comment("Message shown when adding a point to an area. Use {label} for its name")
	public static String MESSAGE_AREA_POINT_ADD = "Added point to area: {label}";
	@Key("messages.area.point-add-failed")
	@Comment("Message shown when adding a point to an area fails. Use {label} for its name")
	public static String MESSAGE_AREA_POINT_ADD_FAILED = "Could not add point to area: {label}";
	@Key("messages.area.remove")
	@Comment("Message shown when removing an area. Use {label} for its name")
	public static String MESSAGE_AREA_REMOVE = "Removed area: {label}";
	@Key("messages.area.point-remove")
	@Comment("Message shown when removing a point from an area. Use {label} for its name")
	public static String MESSAGE_AREA_POINT_REMOVE = "Removed point from area: {label}";
	@Key("messages.area.enter")
	@Comment("Message shown when entering an area. Use {name} for its name")
	public static String MESSAGE_AREA_ENTER = "[+] {name}";
	@Key("messages.area.leave")
	@Comment("Message shown when leaving an area. Use {name} for its name")
	public static String MESSAGE_AREA_LEAVE = "[-] {name}";
	@Key("messages.sign.invalid-text")
	@Comment("Message shown when sign text is invalid")
	public static String MESSAGE_SIGN_INVALID_TEXT = "Text should be a String array with a size of 4";
	@Key("messages.sign.add")
	@Comment("Message shown when adding a sign marker")
	public static String MESSAGE_SIGN_ADD = "Added sign marker";
	@Key("messages.sign.edit")
	@Comment("Message shown when editing a sign marker")
	public static String MESSAGE_SIGN_EDIT = "Edited sign marker";
	@Key("messages.sign.remove")
	@Comment("Message shown when removing a sign marker")
	public static String MESSAGE_SIGN_REMOVE = "Removed sign marker";
	@Key("marker-settings.players.nether-to-overworld")
	@Comment("Show players in the nether transparently on the overworld map")
	public static boolean PLAYERS_NETHER_IN_OVERWORLD = true;
	@Key("marker-settings.players.overworld-to-nether")
	@Comment("Show players in the overworld transparently on the nether map")
	public static boolean PLAYERS_OVERWORLD_IN_NETHER = true;
	@Key("marker-settings.areas.enabled")
	@Comment("Enable player made areas on the map")
	public static boolean AREA_MARKERS_ENABLED = true;
	@Key("marker-settings.areas.priority")
	@Comment("The priority for areas, the lower the number the higher it is on the map")
	public static int AREA_MARKERS_PRIORITY = 50;
	@Key("marker-settings.areas.always-show-name")
	@Comment("Always show the name of areas on the map")
	public static boolean AREA_MARKERS_MARKERS_ALWAYS_SHOW_NAME = true;
	@Key("marker-settings.areas.show-area-size")
	@Comment("Show the area size in square blocks in the pop-up")
	public static boolean AREA_MARKERS_SHOW_SIZE = false;
	@Key("marker-settings.areas.size")
	@Comment("The maximum diameter that an area is allowed to have \nthis is the distance between the two furthest points in the area")
	public static int AREA_MARKERS_MAX_SIZE = 512;
	@Key("marker-settings.nether_portals.enabled")
	@Comment("Enable nether portal markers on the map")
	public static boolean NETHER_PORTAL_MARKERS_ENABLED = true;
	@Key("marker-settings.nether_portals.rename")
	@Comment("Allow nether portal markers to be renamed using name tags")
	public static boolean NETHER_PORTAL_MARKERS_RENAME = true;
	@Key("marker-settings.nether_portals.always-show-name")
	@Comment("Always show the name of nether portals on the map")
	public static boolean NETHER_PORTAL_MARKERS_ALWAYS_SHOW_NAME = true;
	@Key("marker-settings.nether_portals.priority")
	@Comment("The priority for nether portal markers, the lower the number the higher it is on the map")
	public static int NETHER_PORTAL_MARKERS_PRIORITY = 50;
	@Key("marker-settings.beacons.enabled")
	@Comment("Enable beacon markers on the map")
	public static boolean BEACON_MARKERS_ENABLED = true;
	@Key("marker-settings.beacons.priority")
	@Comment("The priority for beacon markers, the lower the number the higher it is on the map")
	public static int BEACON_MARKERS_PRIORITY = 50;
	@Key("marker-settings.end_portals.enabled")
	@Comment("Enable end portal markers on the map")
	public static boolean END_PORTAL_MARKERS_ENABLED = true;
	@Key("marker-settings.end_portals.priority")
	@Comment("The priority for end portal markers, the lower the number the higher it is on the map")
	public static int END_PORTAL_MARKERS_PRIORITY = 50;
	@Key("marker-settings.end_gateways.enabled")
	@Comment("Enable end gateway markers on the map")
	public static boolean END_GATEWAY_MARKERS_ENABLED = true;
	@Key("marker-settings.end_gateways.priority")
	@Comment("The priority for end gateway markers, the lower the number the higher it is on the map")
	public static int END_GATEWAY_MARKERS_PRIORITY = 50;
	@Key("marker-settings.signs.enabled")
	@Comment("Enable sign markers on the map")
	public static boolean SIGN_MARKERS_ENABLED = true;
	@Key("marker-settings.signs.priority")
	@Comment("The priority for sign markers, the lower the number the higher it is on the map")
	public static int SIGN_MARKERS_PRIORITY = 50;
	@Key("marker-settings.signs.always-show-text")
	@Comment("Always show the text of sign markers on the map")
	public static boolean SIGN_MARKERS_ALWAYS_SHOW_TEXT = true;
	@Key("marker-settings.signs.fill_lines")
	@Comment("Fill all 4 lines of a sign in a pop-up, if false, only the lines with text will be shown.")
	public static boolean SIGN_MARKERS_FILL_LINES = false;
	@Key("marker-settings.lightning.enabled")
	@Comment("Enable lightning strike markers on the map")
	public static boolean LIGHTNING_MARKERS_ENABLED = true;
	@Key("marker-settings.lightning.priority")
	@Comment("The priority for lightning strike markers, the lower the number the higher it is on the map")
	public static int LIGHTNING_MARKERS_PRIORITY = 50;
	@Key("marker-settings.lightning.lifetime")
	@Comment("The lifetime of a lightning strike marker in seconds")
	public static int LIGHTNING_MARKERS_LIFETIME = 3;

	public static void reload() {
		CONFIG.reload(Pl3xMarkersCore.getMainDir().resolve("config.yml"), MarkersConfig.class);
	}
}
