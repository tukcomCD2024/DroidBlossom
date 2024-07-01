package site.timecapsulearchive.core.global.common.wrapper;

import java.util.Arrays;

public record ByteArrayWrapper(byte[] data) {

    public ByteArrayWrapper {
        if (data == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ByteArrayWrapper)) {
            return false;
        }
        return Arrays.equals(data, ((ByteArrayWrapper) obj).data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
