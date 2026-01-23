package nl.gjorgdy.pl3xMarkers.paper;

import net.pl3x.map.core.configuration.AbstractConfig;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;

public class PaperMarkersConfig extends AbstractConfig {

	private static final PaperMarkersConfig CONFIG = new PaperMarkersConfig();

	@Key("marker-settings.shopkeepers.enabled")
	@Comment("Enable Shopkeepers shop on the map")
	public static boolean SHOPKEEPERS_MARKERS_ENABLED = true;

	@Key("marker-settings.shopkeepers.priority")
	@Comment("The priority for Shopkeepers shop, the lower the number the higher it is on the map")
	public static int SHOPKEEPERS_MARKERS_PRIORITY = 40;

	public static void reload() {
		MarkersConfig.reload();
		// Append fabric exclusive settings
		CONFIG.reload(Pl3xMarkersCore.getMainDir().resolve("config.yml"), PaperMarkersConfig.class);
	}

}
