package site.timecapsulearchive.core.global.geography;

import org.opengis.referencing.operation.TransformException;
import site.timecapsulearchive.core.global.error.ErrorCode;
import site.timecapsulearchive.core.global.error.exception.BusinessException;

public class GeoTransformException extends BusinessException {

    public GeoTransformException(TransformException e) {
        super(ErrorCode.GEO_TRANSFORMED_ERROR, e);
    }
}
