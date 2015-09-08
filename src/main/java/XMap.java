/**
 * Basic interface for Map functionality
 */
public interface XMap<K, V> {

	/**
	 * @return the number of key-value mappings in this map
	 */
	int size();

	/**
	 * Associates the specified value with the specified key in this map
	 * 
	 * @param key
	 * @param value
	 * @return old value if exists
	 */
	V put(K key, V value);

	/**
	 * Returns the value to which the specified key is mapped,
	 * 
	 * @param key
	 */
	V get(Object key);
}
