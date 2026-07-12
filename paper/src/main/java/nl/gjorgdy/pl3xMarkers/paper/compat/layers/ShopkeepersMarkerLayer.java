package nl.gjorgdy.pl3xMarkers.paper.compat.layers;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xMarkers.paper.PaperMarkersConfig;
import nl.gjorgdy.pl3xMarkers.paper.Pl3xMarkersPaper;
import nl.gjorgdy.pl3xMarkers.paper.compat.helpers.ShopkeeperItemsHelper;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.markers.MarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class ShopkeepersMarkerLayer extends MarkerLayer<Shopkeeper> {

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

	@Override
	public MarkerBuilder<?> createBuilder(Shopkeeper shopkeeper) {
		var loc = shopkeeper.getLocation();
		if (loc == null) {
			throw new IllegalArgumentException("Shopkeeper has no location");
		}
		return IconMarkerBuilder.newIconMarker(
						shopkeeper.getIdString(),
						Icons.Keys.SHOPKEEPERS,
						loc.getBlockX(), loc.getBlockZ()
				)
				.centerIcon(16, 16);
	}

	@Override
	protected @Nullable String createTooltip(Shopkeeper shopkeeper) {
		return HtmlHelper.tooltip(
				shopkeeper.getDisplayName(),
				shopkeeper.getType().getDisplayName(),
				"Click for trades"
		);
	}

	@Override
	protected @Nullable String createPermanentBottomTooltip(Shopkeeper shopkeeper) {
		if (PaperMarkersConfig.SHOPKEEPERS_ALWAYS_SHOW_NAME) {
			return HtmlHelper.sanitize(shopkeeper.getDisplayName());
		}
		return null;
	}

	@Override
	protected @Nullable String createPopup(Shopkeeper shopkeeper) {
		return HtmlHelper.scrollablePopUp(
				shopkeeper.getDisplayName(),
				shopkeeper.getType().getDisplayName(),
				String.join("<br>", shopkeeper.getTradingRecipes(null).stream()
						.map(ShopkeeperItemsHelper::formatTrade).toArray(String[]::new)
				)
		);
	}

	public void loadShopkeeper(Shopkeeper shopkeeper) {
		var loc = shopkeeper.getLocation();
		if (loc == null || !worldIdentifier.equals(loc.getWorld().getName())) {
			return; // skip shopkeepers from other worlds
		}
		if (hasMarker(shopkeeper.getIdString())) {
			removeMarker(shopkeeper.getIdString());
		}
		loadMarker(shopkeeper);
	}

	public void removeShopkeeper(Shopkeeper shopkeeper) {
		if (hasMarker(shopkeeper.getIdString())) {
			removeMarker(shopkeeper.getIdString());
		}
	}
}
