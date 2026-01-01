package nl.gjorgdy.pl3xmarkers.fabric;

import net.pl3x.map.core.configuration.AbstractConfig;
import nl.gjorgdy.pl3xmarkers.core.MarkersConfig;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;

public class FabricMarkersConfig extends AbstractConfig {

	private static final FabricMarkersConfig CONFIG = new FabricMarkersConfig();

	public static void reload() {
		MarkersConfig.reload();
		// Append fabric exclusive settings
		CONFIG.reload(Pl3xMarkersCore.getMainDir().resolve("config.yml"), FabricMarkersConfig.class);
	}

	@Key("marker-settings.open-parties-and-claims.enabled")
	@Comment("Enable opac claims on the map")
	public static boolean OPAC_MARKERS_ENABLED = true;

	@Key("marker-settings.open-parties-and-claims.priority")
	@Comment("The priority for opac claims, the lower the number the higher it is on the map")
	public static int OPAC_MARKERS_PRIORITY = 40;

}
