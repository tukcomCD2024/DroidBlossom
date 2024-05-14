package site.timecapsulearchive.core.global.common.wrapper;

import java.util.Arrays;

public final class ByteArrayWrapper {

    private final byte[] data;

    public ByteArrayWrapper(final byte[] data) {
        if (data == null) {
            throw new NullPointerException();
        }
        this.data = data;
    }

    public byte[] getData() {
        return data;
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
