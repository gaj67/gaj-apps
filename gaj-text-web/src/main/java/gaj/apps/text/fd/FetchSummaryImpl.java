package gaj.apps.text.fd;

/*package-private*/ class FetchSummaryImpl implements FetchSummary {

    private final String word;
    private final boolean isFound;
    private final boolean isFetched;
    private final String error;
    private int wordCount = 1;

    /*package-private*/ FetchSummaryImpl(String word, boolean isFound, boolean isFetched, String error) {
        this.word = word;
        this.isFound = isFound;
        this.isFetched = isFetched;
        this.error = error;
    }

    @Override
    public String getWord() {
        return word;
    }

    @Override
    public boolean isFound() {
        return isFound;
    }

    @Override
    public boolean isFetched() {
        return isFetched;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public int getWordCount() {
        return wordCount;
    }

    /**
     * Increments the number of times this word was attempted to be fetched (in
     * the local process).
     */
    void incWordCount() {
        wordCount++;
    }

    // ********************************************************
    /*package-private*/ static FetchSummary noPath(String word) {
        return new FetchSummaryImpl(word, false, false, "No file path found");
    }

    /*package-private*/ static FetchSummary fileFound(String word) {
        return new FetchSummaryImpl(word, true, false, null);
    }

    /*package-private*/ static FetchSummary fileFetched(String word) {
        return new FetchSummaryImpl(word, false, true, null);
    }

    /*package-private*/ static FetchSummary fileNotFetched(String word, String error) {
        return new FetchSummaryImpl(word, false, false, error);
    }

}
