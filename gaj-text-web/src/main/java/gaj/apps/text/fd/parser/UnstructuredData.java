package gaj.apps.text.fd.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple wrapper for a backing map from string keys to unstructured object
 * values.
 */
public interface UnstructuredData {

    /**
     * Obtains the backing map from string keys to unstructured object values.
     * 
     * @return The map of data.
     */
	Map<String, Object> getMap();
	
	/**
     * Creates an unstructured instance from a map.
     * 
     * @param map
     *            - The map of data.
     * @return The unstructured instance.
     */
    static UnstructuredData from(Map<String, Object> map) {
        return new UnstructuredData() {
            @Override
            public Map<String, Object> getMap() {
                return map;
            }

            @Override
            public String toString() {
                return map.toString();
            }
        };
    }

    /**
     * Creates an empty unstructured instance.
     * 
     * @return The unstructured instance.
     */
    static UnstructuredData create() {
        return from(new HashMap<>());
    }

}
