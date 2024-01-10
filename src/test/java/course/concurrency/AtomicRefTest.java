package course.concurrency;

import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class AtomicRefTest {
    @Test
    public void atomicRefCASTest() {
        TestObject to = new TestObject(0, "0");
        AtomicReference<TestObject> ref = new AtomicReference<>(to);
        assertFalse(ref.compareAndSet(new TestObject(0, "0"), new TestObject(1, "1")));
        assertTrue(ref.compareAndSet(to, new TestObject(1, "1")));
    }

    public static final class TestObject {
        private final int i;
        private final String s;

        public TestObject(int i, String s) {
            this.i = i;
            this.s = s;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestObject that = (TestObject) o;
            return i == that.i && Objects.equals(s, that.s);
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, s);
        }
    }
}
