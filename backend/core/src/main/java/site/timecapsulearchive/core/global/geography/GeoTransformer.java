package site.timecapsulearchive.core.global.geography;

import lombok.RequiredArgsConstructor;
import org.geotools.geometry.jts.JTS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeoTransformer {

    private static final int SRID_3857 = 3857;
    private static final int DISTANCE_MITER_UNIT = 1000;

    private final GeometryFactory geometryFactoryOf4326;
    private final GeometryFactory geometryFactoryOf3857;

    private final MathTransform mathTransform4326To3857;
    private final MathTransform mathTransform3857To4326;

    /**
     * SRID 4326 좌표를 받아서 SRID 3857 Point 객체를 반환한다.
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return Point 변환된 SRID 3857 Point 객체
     * @throws GeoTransformException 잘못된 값의 범위로 인해 오류가 발생할 수 있다.
     */
    public Point changePoint4326To3857(double latitude, double longitude) {
        Point point = geometryFactoryOf4326.createPoint(new Coordinate(latitude, longitude));

        try {
            Point transPoint = (Point) JTS.transform(point, mathTransform4326To3857);
            transPoint.setSRID(SRID_3857);

            return transPoint;
        } catch (TransformException e) {
            throw new GeoTransformException(e);
        }
    }

    /**
     * SRID 3857 좌표와 범위를 받아서 최소 범위 사각형을 만든다.
     *
     * @param point    SRID 3857 좌표
     * @param distance 범위 거리
     * @return Polygon 최소 범위 사각형인 Polygon 객체
     */
    public Polygon getDistanceMBROf3857(Point point, double distance) {
        double x = point.getX();
        double y = point.getY();

        double minX = x - distance * DISTANCE_MITER_UNIT;
        double minY = y - distance * DISTANCE_MITER_UNIT;
        double maxX = x + distance * DISTANCE_MITER_UNIT;
        double maxY = y + distance * DISTANCE_MITER_UNIT;

        Polygon polygon = geometryFactoryOf3857.createPolygon(new Coordinate[]{
            new Coordinate(minX, minY),
            new Coordinate(maxX, minY),
            new Coordinate(maxX, maxY),
            new Coordinate(minX, maxY),
            new Coordinate(minX, minY)
        });
        polygon.setSRID(SRID_3857);

        return polygon;
    }

    /**
     * SRID 4326 좌표를 받아서 SRID 3857 좌표로 변환한다.
     *
     * @param point SRID 4326 좌표인 Point 객체
     * @return Point SRID 3857 좌표인 Point 객체
     * @throws GeoTransformException 잘못된 값의 범위로 인해 오류가 발생할 수 있다.
     */
    public Point changePoint3857To4326(Point point) {
        try {
            return (Point) JTS.transform(point, mathTransform3857To4326);
        } catch (TransformException e) {
            throw new GeoTransformException(e);
        }
    }
}
