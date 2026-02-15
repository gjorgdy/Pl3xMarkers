package nl.gjorgdy.pl3xMarkers.paper.listeners;

import nl.gjorgdy.pl3xMarkers.paper.MinecartPathingRegistry;
import nl.gjorgdy.pl3xMarkers.paper.helpers.StorageHelper;
import nl.gjorgdy.pl3xmarkers.core.Api;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.LineMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class MinecartListener implements Listener {

	private final MinecartPathingRegistry minecartPathingRegistry;

	public MinecartListener(MinecartPathingRegistry minecartPathingRegistry) {
		this.minecartPathingRegistry = minecartPathingRegistry;
	}

	@EventHandler
	public void onMinecartMove(VehicleMoveEvent event) {
		if (event.getVehicle().getType() == org.bukkit.entity.EntityType.MINECART) {
			if (event.getVehicle().getPassengers().isEmpty()) {
				return;
			}
			// left from station : start pathing
			if (isStation(event.getFrom()) && !isStation(event.getTo())) {
				minecartPathingRegistry.endPath(event.getVehicle().getUniqueId());
				System.out.println("Left station: " + event.getTo());
				minecartPathingRegistry.pathPoint(event.getVehicle().getUniqueId(), event.getTo().toCenterLocation());
				return;
			}
			// minecart not going straight : add path point
			if (event.getVehicle().getYaw() % 90 != 0) {
				System.out.println("Minecart is turning: " + event.getTo());
				minecartPathingRegistry.pathPoint(event.getVehicle().getUniqueId(), event.getTo().toCenterLocation());
				return;
			}
			// arrived at station : stop pathing
			if (isStation(event.getTo()) && event.getVehicle().getVelocity().length() < 0.3) {
				System.out.println("Arrived at station: " + event.getTo());
				var path = minecartPathingRegistry.endPath(event.getVehicle().getUniqueId());
				if (path == null) {
					System.out.println("No path found for minecart: " + event.getVehicle().getUniqueId());
					return;
				}
				for (var loc : path) {
					System.out.println("Path point: " + loc);
				}
				var layer = Api.getMarkerLayer(event.getVehicle().getWorld().getName(), Layers.Keys.RAILLINES, LineMarkerLayer.class);
				if (layer != null) {
					layer.createLine(StorageHelper.toJsonStoragePoints(path));
				}
			}
		}
	}

	@EventHandler
	public void onRemove(VehicleDestroyEvent event) {
		if (event.getVehicle().getType() == org.bukkit.entity.EntityType.MINECART) {
			minecartPathingRegistry.endPath(event.getVehicle().getUniqueId());
			System.out.println("Minecart destroyed: " + event.getVehicle().getLocation());
		}
	}

	private boolean isStation(Location location) {
		return location.getBlock().getType() == Material.POWERED_RAIL &&
					   location.add(0, -1, 0).getBlock().getType() == Material.LODESTONE;
	}

}
