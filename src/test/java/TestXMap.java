import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests
 */
public class TestXMap extends Assert {
    XMap map;

    @Before
    public void init() {
        map = new XHashMap();
    }

    @Test
    public void testPutAndGet() {
        putAndGet(1, 2L); //usual case
        putAndGet(9999, 3L); // key > capacity
        putAndGet(-9999, -2L); // key < 0
    }

    @Test
    public void testFullTable() {
        map.put(9999, 9999L);
        map.put(9999 + XHashMap.CAPACITY, 9998L);
        for (int i = 2000; i < XHashMap.CAPACITY * XHashMap.LOAD_FACTOR - 2 + 2000; i++) {
            map.put(i, new Long(i));
        }
        for (int i = 2000; i < XHashMap.CAPACITY * XHashMap.LOAD_FACTOR - 2 + 2000; i++) {
            assertEquals(new Long(i), map.get(i));
        }

        assertEquals(9999L, map.get(9999));
        assertEquals(9998L, map.get(9999 + XHashMap.CAPACITY));

        map.put(9997, 9997L);
        assertNotEquals(9997L, map.get(9997));
        assertNull(map.get(9997));
    }

    @Test
    public void testSize() {
        assertEquals(0, map.size());

        map.put(1, 1L);
        assertEquals(1, map.size());

        for (int i = 0; i < XHashMap.CAPACITY; i++) {
            map.put(i, new Long(i));
        }

        assertEquals((int) Math.ceil(XHashMap.CAPACITY*XHashMap.LOAD_FACTOR), map.size()); //max size
    }

    @Test
    public void testNull() {
        assertNull(map.get(0));
        assertNull(map.get(1));
        assertNull(map.get(null));
    }

    @Test
    public void testNullAfterPut() {
        map.put(2, 3L);
        assertNull(map.get(0));
        assertNull(map.get(1));
        assertNull(map.get(null));
    }

    @Test(expected = ClassCastException.class)
    public void testCastException() {
        map.put(3L, 3);
    }

    @Test()
    public void testNotIntegerKey() {
        map.put(3, 3L);
        assertNull(map.get(3L));
    }

    private void putAndGet(Integer key, Long value) {
        map.put(key, value);
		assertEquals(value, map.get(key));
	}
}
