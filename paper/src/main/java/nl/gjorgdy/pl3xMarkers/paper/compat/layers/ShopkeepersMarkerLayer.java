package nl.gjorgdy.pl3xMarkers.paper.compat.layers;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xMarkers.paper.PaperMarkersConfig;
import nl.gjorgdy.pl3xMarkers.paper.Pl3xMarkersPaper;
import nl.gjorgdy.pl3xMarkers.paper.compat.helpers.ShopkeeperItemsHelper;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.intellij.lang.annotations.Language;
import org.jspecify.annotations.NonNull;

public class ShopkeepersMarkerLayer extends MarkerLayer {

	public ShopkeepersMarkerLayer(@NonNull World world) {
		super(Layers.Keys.SHOPKEEPERS, Layers.Labels.SHOPKEEPERS, world, PaperMarkersConfig.SHOPKEEPERS_MARKERS_PRIORITY);
	}

	@Override
	public void load() {
		var plugin = Pl3xMarkersPaper.getPlugin(Pl3xMarkersPaper.class);
		if (ShopkeepersAPI.isEnabled()) {
			ShopkeepersAPI.getShopkeeperRegistry().getAllShopkeepers().forEach(this::loadShopkeeper);
		} else {
			plugin.getLogger().warning("Shopkeepers plugin is not enabled, cannot load shopkeeper markers.");
		}
	}

	public void loadShopkeeper(Shopkeeper shopkeeper) {
		var loc = shopkeeper.getLocation();
		if (loc == null || !worldIdentifier.equals(loc.getWorld().getName())) {
			return; // skip shopkeepers from other worlds
		}
		if (hasMarker(shopkeeper.getIdString())) {
			removeMarker(shopkeeper.getIdString());
		}
		addMarker(createIconMarker(shopkeeper));
	}

	public void removeShopkeeper(Shopkeeper shopkeeper) {
		if (hasMarker(shopkeeper.getIdString())) {
			removeMarker(shopkeeper.getIdString());
		}
	}

	protected Marker<?> createIconMarker(Shopkeeper shopkeeper) {
		var loc = shopkeeper.getLocation();
		if (loc == null) {
			throw new IllegalArgumentException("Shopkeeper has no location");
		}
		@Language("HTML") var tooltip = HtmlHelper.tooltip(
				shopkeeper.getDisplayName(),
				shopkeeper.getType().getDisplayName(),
				"Click for trades"
		);
		@Language("HTML") var popup = HtmlHelper.scrollablePopUp(
				shopkeeper.getDisplayName(),
				shopkeeper.getType().getDisplayName(),
				String.join("<br>", shopkeeper.getTradingRecipes(null).stream()
											.map(ShopkeeperItemsHelper::formatTrade).toArray(String[]::new))
		);
		return IconMarkerBuilder.newIconMarker(
						shopkeeper.getIdString(),
						Icons.Keys.SHOPKEEPERS,
						loc.getBlockX(), loc.getBlockZ()
				)
					   .centerIcon(16, 16)
					   .addTooltip(tooltip)
					   .addPopup(popup)
					   .build();
	}
}
