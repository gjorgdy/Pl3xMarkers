package nl.gjorgdy.pl3xMarkers.paper.compat.helpers;

import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.Locale;

public class ShopkeeperItemsHelper {

	public static String getItemName(UnmodifiableItemStack item) {
		if (item.getItemMeta() != null) {
			Component displayName = item.getItemMeta().displayName();
			if (displayName != null && !serialize(displayName).isEmpty()) {
				return serialize(displayName);
			}
		}
		return item.getType().name().toLowerCase(Locale.ROOT).replace("_", " ");
	}

	public static String serialize(Component component) {
		return PlainTextComponentSerializer.plainText().serialize(component);
	}

	public static String formatTrade(TradingRecipe tradingRecipe) {
		var sb = new StringBuilder();
		if (tradingRecipe.isOutOfStock()) {
			sb.append("<del>");
		}
		String firstItemName = getItemName(tradingRecipe.getItem1());
		int firstItemAmount = tradingRecipe.getItem1().getAmount();
		sb.append(firstItemAmount).append("x ").append(firstItemName);
		if (tradingRecipe.getItem2() != null) {
			String secondItemName = getItemName(tradingRecipe.getItem2());
			int secondItemAmount = tradingRecipe.getItem2().getAmount();
			sb.append(" + ").append(secondItemAmount).append("x ").append(secondItemName);
		}
		String resultItemName = getItemName(tradingRecipe.getResultItem());
		int resultItemAmount = tradingRecipe.getResultItem().getAmount();
		sb.append(" → ").append(resultItemAmount).append("x ").append(resultItemName);
		if (tradingRecipe.isOutOfStock()) {
			sb.append("</del>");
		}
		return sb.toString();
	}

}
