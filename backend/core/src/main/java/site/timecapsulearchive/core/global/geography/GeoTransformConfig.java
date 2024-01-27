package site.timecapsulearchive.core.global.geography;

import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeoTransformConfig {

    private static final String EPSG_4326 = "EPSG:4326";
    private static final String EPSG_3857 = "EPSG:3857";
    private static final int SRID_4326 = 4326;
    private static final int SRID_3857 = 3857;

    @Bean
    public MathTransform mathTransform4326To3857() throws FactoryException {
        CoordinateReferenceSystem sourceCrs = CRS.decode(EPSG_4326);
        CoordinateReferenceSystem targetCrs = CRS.decode(EPSG_3857);

        return CRS.findMathTransform(sourceCrs, targetCrs, true);
    }

    @Bean
    public MathTransform mathTransform3857To4326() throws FactoryException {
        CoordinateReferenceSystem sourceCrs = CRS.decode(EPSG_3857);
        CoordinateReferenceSystem targetCrs = CRS.decode(EPSG_4326);

        return CRS.findMathTransform(sourceCrs, targetCrs, true);
    }

    @Bean
    public GeometryFactory geometryFactoryOf4326() {
        return new GeometryFactory(new PrecisionModel(), SRID_4326);
    }

    @Bean
    public GeometryFactory geometryFactoryOf3857() {
        return new GeometryFactory(new PrecisionModel(), SRID_3857);
    }
}
