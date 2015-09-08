import java.util.Map;

/**
 * Simple realisation of Hash Map with open addressing and linear probing
 */
public class XHashMap implements XMap<Integer, Long> {
	public static final int LINEAR_INTERVAL = 1;
	public static final int CAPACITY = 1024; // capacity and load factor are
												// immutable
	public static final float LOAD_FACTOR = 0.75F;

	int size = 0;
	XEntry[] table = new XEntry[CAPACITY];

	/**
	 * A map entry (key-value pair).
	 */
	class XEntry implements Map.Entry<Integer, Long> {
		final int key;
		long value;

		public XEntry(Integer key, Long value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public Integer getKey() {
			return key;
		}

		@Override
		public Long getValue() {
			return value;
		}

		@Override
		public final Long setValue(Long newValue) {
			Long oldValue = value;
			value = newValue;
			return oldValue;
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Long put(Integer key, Long value) {
		/*
		 * In this realisation of hash map, when threshold is get, map blocks
		 * adding elements instead of table rebuild
		 */
		if (size() >= CAPACITY * LOAD_FACTOR)
			return null;

		int index = getIndex(key.hashCode());
		return putOrWalk(index, key, value);
	}

	@Override
	public Long get(Object key) {
		if (key instanceof Integer) {
			Integer castedKey = (Integer) key;
			int index = getIndex(castedKey.hashCode());
			return getOrWalk(index, castedKey);
		}
		return null;
	}

	/**
	 * Realisation of open addressing algorithm. This method will be called
	 * recursively until free cell or equal existing key would be found
	 * 
	 * @param index
	 *            - current index
	 */
	private Long putOrWalk(int index, Integer key, Long value) {
		XEntry currentEntry = table[index];
		if (currentEntry != null) {
			if (currentEntry.getKey().equals(key)) {
				// update value of existing key
				Long oldValue = currentEntry.getValue();
				currentEntry.setValue(value);
				return oldValue;
			} else {
				return putOrWalk(getIndex(newIndex(index)), key, value);
			}
		}

		// add new entry to free cell
		table[index] = new XEntry(key, value);
		size++;
		return null;
	}

	/**
	 * Realisation of open addressing algorithm. This method will be called
	 * recursively until free cell or equal existing key would be found
	 * 
	 * @param index
	 *            - current index
	 */
	private Long getOrWalk(int index, Integer key) {
		XEntry currentEntry = table[index];
		if (currentEntry != null) {
			if (currentEntry.getKey().equals(key)) {
				return currentEntry.getValue();
			} else {
				return getOrWalk(getIndex(newIndex(index)), key);
			}
		}

		// if free cell is found there is no element with current key
		return null;
	}

	/**
	 * Index that based on hash must be < table.length
	 */
	private int getIndex(int hash) {
		return (hash & 0x7FFFFFFF) % table.length;
	}

	/**
	 * Realisation of linear probing algorithm
	 */
	protected int newIndex(int index) {
		return index + LINEAR_INTERVAL;
	}

}
