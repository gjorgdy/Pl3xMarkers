package nl.gjorgdy.pl3xmarkers.repositories;

import nl.gjorgdy.pl3xmarkers.Database;
import nl.gjorgdy.pl3xmarkers.entities.IconMarkerAdditionEntity;
import nl.gjorgdy.pl3xmarkers.entities.IconMarkerEntity;
import nl.gjorgdy.pl3xmarkers.objects.MarkerIdentifier;

import java.sql.SQLException;
import java.util.function.Consumer;

public class IconMarkerAdditionsRepository extends IconMarkerRepository {

    public IconMarkerAdditionsRepository(Database database) {
        super(database);
    }

    public IconMarkerEntity updateName(MarkerIdentifier identifier, String label) {
        return updateAdditions(identifier, additions -> additions.setName(label));
    }

    public IconMarkerEntity updateColor(MarkerIdentifier identifier, int color) {
        return updateAdditions(identifier, additions -> additions.setColor(color));
    }

    private IconMarkerEntity updateAdditions(MarkerIdentifier identifier, Consumer<IconMarkerAdditionEntity> action) {
        try {
            var marker = getMarker(identifier.WorldKey(), identifier.LayerKey(), identifier.x(), identifier.z());
            if (marker == null) {
                return null;
            }
            var additions = marker.getAdditionsOrEmpty();
            action.accept(additions);
            var status = database.iconMarkerAdditions.createOrUpdate(additions);
            return getMarker(identifier.WorldKey(), identifier.LayerKey(), identifier.x(), identifier.z());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
