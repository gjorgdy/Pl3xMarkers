package nl.gjorgdy.pl3xmarkers.core.layers;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.layers.primitive.NamedIconMarkerLayer;
import nl.gjorgdy.pl3xmarkers.core.registries.Icons;
import nl.gjorgdy.pl3xmarkers.core.registries.Layers;

public class SignsIconMarkerLayer extends NamedIconMarkerLayer {

	public SignsIconMarkerLayer(World world, int priority) {
		super(Icons.Keys.SIGN, Layers.Keys.SIGNS, Layers.Labels.SIGNS, "", world, priority);
	}

}
