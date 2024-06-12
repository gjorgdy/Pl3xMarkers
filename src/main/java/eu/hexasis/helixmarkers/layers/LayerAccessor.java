package eu.hexasis.helixmarkers.layers;

import eu.hexasis.helixmarkers.markers.MarkerBuilder;

public class LayerAccessor {

    private final MarkerLayer markerLayer;

    public LayerAccessor(MarkerLayer layer) {
        this.markerLayer = layer;
    }

    public void addMarker(MarkerBuilder markerBuilder) {
        markerLayer.addMarker(markerBuilder);
    }

    public void removeMarker(String markerKey) {
        markerLayer.removeMarker(markerKey);
    }

}
