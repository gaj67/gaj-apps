package gaj.apps.text.fd;

/**
 * Provides a summary of the success or otherwise of fetching a word definition file.
 */
public interface FetchSummary {

    /**
     * Obtains the word for which a definition file should be fetched.
     * 
     * @return The word.
     */
    String getWord();

    /**
     * Indicates if the word definition file already exists locally.
     * 
     * @return A value of true (or false) if the word definition file was (or
     *         was not) found locally.
     */
    default boolean isFound() {
        return false;
    }

    /**
     * Indicates if the word definition file was just fetched.
     * 
     * @return A value of true (or false) if the word definition file was (or
     *         was not) just fetched.
     */
    default boolean isFetched() {
        return false;
    }

    /**
     * Indicates the reason why fetching the word definition file did not
     * succeed.
     * 
     * @return The error message, or a value of null if there was no error.
     */
    default /*@Nullable*/ String getError() {
        return null;
    }

    // ********************************************************************************
    // Helper methods.

    /**
     * Provides a summary for which the word definition file was already found locally.
     * 
     * @param word - The word.
     * @return The summary.
     */
    static FetchSummary fileFound(String word) {
        return new FetchSummary() {
            @Override
            public String getWord() {
                return word;
            }

            @Override
            public boolean isFound() {
                return true;
            }
        };
    }

    /**
     * Provides a summary for which the word definition file was not found locally
     * but was successfully fetched.
     * 
     * @param word - The word.
     * @return The summary.
     */
    static FetchSummary fileFetched(String word) {
        return new FetchSummary() {
            @Override
            public String getWord() {
                return word;
            }

            @Override
            public boolean isFetched() {
                return true;
            }
        };
    }

    /**
     * Provides a summary for which the word definition file was not found locally
     * and was not successfully fetched.
     * 
     * @param word - The word.
     * @param error - The error message.
     * @return The summary.
     */
    static FetchSummary fileNotFetched(String word, String error) {
        return new FetchSummary() {
            @Override
            public String getWord() {
                return word;
            }

            @Override
            public String getError() {
                return error;
            }
        };
    }

}
