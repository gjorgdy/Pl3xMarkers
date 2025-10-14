package nl.gjorgdy.pl3xMarkers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityTeleportEvent;

public class NetherPortalListener implements Listener {

    @EventHandler
    public void onPortalEnter(EntityTeleportEvent event) {
        System.out.println("Entered portal at " + event.getFrom() + " " + event.getEntity().getLocation().getBlock());
    }

}
