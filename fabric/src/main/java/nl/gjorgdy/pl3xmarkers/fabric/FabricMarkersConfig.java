package nl.gjorgdy.pl3xmarkers.fabric;

import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;

public class FabricMarkersConfig extends MarkersConfig {

	private static final FabricMarkersConfig CONFIG = new FabricMarkersConfig();

	public static void reload() {
		CONFIG.reload(Pl3xMarkersCore.getMainDir().resolve("config.yml"), FabricMarkersConfig.class);
	}

	@Key("marker-settings.open-parties-and-claims.enabled")
	@Comment("Enable opac claims on the map")
	public static boolean OPAC_MARKERS_ENABLED = true;

	@Key("marker-settings.open-parties-and-claims.priority")
	@Comment("The priority for opac claims, the lower the number the higher it is on the map")
	public static int OPAC_MARKERS_PRIORITY = 40;

}
