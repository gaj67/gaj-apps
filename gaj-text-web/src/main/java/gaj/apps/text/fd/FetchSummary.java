package gaj.apps.text.fd;

/**
 * Provides a summary of the success or otherwise of fetching a word definition.
 */
public interface FetchSummary {

    /**
     * Obtains the word for which a definition should be fetched.
     * 
     * @return The word.
     */
    String getWord();

    /**
     * Indicates if the word definition file had previously been fetched.
     * 
     * @return A value of true (or false) if the word definition file was (or
     *         was not) already found.
     */
    boolean wasFound();

    /**
     * Indicates if the word definition file was just fetched.
     * 
     * @return A value of true (or false) if the word definition file was (or
     *         was not) just fetched.
     */
    boolean wasFetched();

    /**
     * Indicates the reason why fetching the word definition file did not
     * succeed.
     * 
     * @return The error message, or a value of null if there was no error.
     */
    /*@Nullable*/ String getError();

    // ****************************************
    // Methods related to multiple fetches by a single, local process.

    /**
     * Obtains the number of times this word was attempted to be fetched (in the
     * local process).
     * 
     * @return The word (token) count.
     */
    int getWordCount();

}
