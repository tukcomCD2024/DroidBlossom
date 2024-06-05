package site.timecapsulearchive.core.global.util;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public final class SliceUtil {

    private SliceUtil() {

    }

    public static <T> Slice<T> makeSlice(final int size, final List<T> dtos) {
        final boolean hasNext = dtos.size() > size;
        if (hasNext) {
            dtos.remove(size);
        }

        return new SliceImpl<>(dtos, Pageable.ofSize(size), hasNext);
    }
}
