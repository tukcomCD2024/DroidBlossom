package site.timecapsulearchive.core.global.geography;

import lombok.RequiredArgsConstructor;
import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeoTransformer {

    private final GeometryFactory geometryFactory4326;

    private final MathTransform mathTransform4326To3857;

    public Point changePoint4326To3857(Double latitude, Double longitude) throws GeoTransformException {
        Point point = geometryFactory4326.createPoint(new Coordinate(latitude, longitude));

        try {
            Point transPoint = (Point) JTS.transform(point, mathTransform4326To3857);
            transPoint.setSRID(3857);

            return transPoint;
        } catch (TransformException e) {
            throw new GeoTransformException(e);
        }
    }
}
