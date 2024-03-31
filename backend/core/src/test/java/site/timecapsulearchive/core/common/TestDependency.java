package site.timecapsulearchive.core.common;

import org.opengis.referencing.FactoryException;
import site.timecapsulearchive.core.global.geography.GeoTransformConfig;
import site.timecapsulearchive.core.global.geography.GeoTransformManager;
import site.timecapsulearchive.core.infra.s3.config.S3Config;
import site.timecapsulearchive.core.infra.s3.config.S3Properties;
import site.timecapsulearchive.core.infra.s3.manager.S3PreSignedUrlManager;
import site.timecapsulearchive.core.infra.s3.manager.S3UrlGenerator;

public class TestDependency {

    public static S3PreSignedUrlManager s3PreSignedUrlManager() {
        S3Config s3Config = new S3Config(
            new S3Properties("a".repeat(32), "b".repeat(32), "origin", "temporary", "us-east"));
        return new S3PreSignedUrlManager(s3Config, new S3UrlGenerator());
    }

    public static GeoTransformManager geoTransformManager() throws FactoryException {
        GeoTransformConfig geoTransformConfig = new GeoTransformConfig();

        return new GeoTransformManager(geoTransformConfig.geometryFactoryOf4326(),
            geoTransformConfig.geometryFactoryOf3857(),
            geoTransformConfig.mathTransform4326To3857(),
            geoTransformConfig.mathTransform3857To4326()
        );
    }
}
