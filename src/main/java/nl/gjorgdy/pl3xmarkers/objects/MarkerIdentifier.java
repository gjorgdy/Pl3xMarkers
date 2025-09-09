package nl.gjorgdy.pl3xmarkers.objects;

public record MarkerIdentifier(String WorldKey, String LayerKey, int x, int z) {

    public LayerIdentifier layerIdentifier() {
        return new LayerIdentifier(this.WorldKey, this.LayerKey);
    }

}
