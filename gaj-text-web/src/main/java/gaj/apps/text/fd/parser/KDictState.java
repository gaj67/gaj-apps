package gaj.apps.text.fd.parser;

/**
 * Indicates the possible macro-states that a section kdict handler may take.
 */
/*package-private*/ enum KDictState {

	// Default state:
	/** 
	 * Indicates the initial or final state. 
	 * May transition to {@link #SECTION}. 
	 */
	INIT(null),

	// Structural states:
	/**
     * Indicates that processing of a section has started. May transition to
     * {@link #INIT}, {@link #WORD}, {@ #WORD_VARIANT}, {@link #TAG} or
     * {@link #SEGMENT}.
     */
	SECTION(false),
	/**
     * Indicates that a segment is being handled. May transition to
     * {@link #SECTION} or {@link #EXAMPLE}.
     */
	SEGMENT(false),

	// Textual states:
    /**
     * Indicates the collection of main word of the section.
     */
	WORD(true),
    /**
     * Indicates the collection of a word variant specified in a segment.
     */
    WORD_VARIANT(true),
	/**
     * Indicates the collection of a segment word tag.
     */
	TAG(true),
	/**
     * Indicates the collection of a segment example.
     */
	EXAMPLE(true),
	/**
	 * Indicates that the state history should be rewound to the parent state.
	 */
	REWIND(null),
    /**
     * Indicates some unexpected structural element that should be ignored.
     */
    OTHER(null), 
	;

	private final boolean isTextual;
	private final boolean isStructural;

	private KDictState(Boolean isTextual) {
		this.isTextual = isTextual != null && isTextual;
		this.isStructural = isTextual != null && !isTextual;
		
	}

	/*package-private*/ boolean isTextual() {
		return isTextual;
	}

	/*package-private*/ boolean isStructural() {
		return isStructural;
	}
	
}
