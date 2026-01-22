package nl.gjorgdy.pl3xMarkers.paper.compat.layers;

import com.nisovin.shopkeepers.api.ShopkeepersAPI;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import net.pl3x.map.core.markers.marker.Marker;
import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xMarkers.paper.Pl3xMarkersPaper;
import nl.gjorgdy.pl3xMarkers.paper.compat.helpers.ShopkeeperItemsHelper;
import nl.gjorgdy.pl3xmarkers.core.helpers.HtmlHelper;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.MarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.markers.IconMarkerBuilder;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.scheduler.BukkitRunnable;
import org.intellij.lang.annotations.Language;
import org.jspecify.annotations.NonNull;

public class ShopkeepersMarkerLayer extends MarkerLayer {

	public ShopkeepersMarkerLayer(@NonNull World world) {
		super(Layers.Keys.SHOPKEEPERS, Layers.Labels.SHOPKEEPERS, world, 50);
	}

	@Override
	public void load() {
		var plugin = Pl3xMarkersPaper.getPlugin(Pl3xMarkersPaper.class);
		new BukkitRunnable() {
			@Override
			public void run() {
				if (ShopkeepersAPI.isEnabled()) {
					ShopkeepersAPI.getShopkeeperRegistry().getAllShopkeepers().forEach(sk -> {
						var loc = sk.getLocation();
						if (loc == null || !worldIdentifier.equals(loc.getWorld().getName())) {
							return; // skip shopkeepers from other worlds
						}
						if (hasMarker(sk.getIdString())) {
							removeMarker(sk.getIdString());
						}
						addMarker(createIconMarker(sk));
					});
				} else {
					plugin.getLogger().warning("Shopkeepers plugin is not enabled, cannot load shopkeeper markers.");
				}
			}
		}.runTaskTimer(plugin, 0L, 20L * 60L); // run every minute to update shopkeepers
	}

	protected Marker<?> createIconMarker(Shopkeeper shopkeeper) {
		var loc = shopkeeper.getLocation();
		if (loc == null) {
			throw new IllegalArgumentException("Shopkeeper has no location");
		}
		@Language("HTML") var tooltip = "<b>" + HtmlHelper.sanitize(shopkeeper.getDisplayName()) + "</b><br>";
		tooltip += String.join("<br>", shopkeeper.getTradingRecipes(null).stream()
											   .map(ShopkeeperItemsHelper::formatTrade).toArray(String[]::new));
		return IconMarkerBuilder.newIconMarker(
						shopkeeper.getIdString(),
						Icons.Keys.SHOPKEEPERS,
						loc.getBlockX(), loc.getBlockZ()
				)
					   .centerIcon(16, 16)
					   .addTooltip(tooltip)
					   .build();
	}
}
