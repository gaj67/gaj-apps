package gaj.apps.text.fd;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import org.apache.commons.io.IOUtils;
import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import gaj.apps.text.fd.parser.SectionsHandler;
import gaj.apps.text.fd.parser.UnstructuredData;

public abstract class FDUtils {

	private static final String WEB_USER_AGENTS = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
	private static final String DICT_DIR = "data/fd";
	private static final String DICT_FILE_TYPE = ".htm";
	private static final String FD_URI = "http://www.thefreedictionary.com/";

	private FDUtils() {}

    /**
     * Obtains the logical path where the word definition file is or should be
     * stored.
     * 
     * @param word - The word.
     * @return The word definition file path.
     * @throws RuntimeException if anything goes wrong.
     */
	public static Path getWordDefinitionFilePath(String word) {
		String lword = word.toLowerCase();
		final int length = lword.length();
		if (length >= 3) {
			return Paths.get(DICT_DIR, lword.substring(0, 1), 
					lword.substring(0, 2), lword.substring(0, 3), lword + DICT_FILE_TYPE);
		} else if (length == 2) {
			return Paths.get(DICT_DIR, lword.substring(0, 1), 
					lword.substring(0, 2), lword + DICT_FILE_TYPE);
		} else if (length == 1) {
			return Paths.get(DICT_DIR, lword.substring(0, 1), lword + DICT_FILE_TYPE);
		}
        throw new IllegalArgumentException("Invalid word: " + word);
	}
	
	/**
	 * Reads a file as a string.
	 * 
	 * @param file - The file path.
	 * @return The file contents string.
     * @throws RuntimeException if anything goes wrong.
	 */
	public static String readFileAsString(Path file) {
		try (InputStream is = Files.newInputStream(file)) {
			return IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
            throw new IllegalStateException(e);
		}
	}

	/**
	 * Fetches the word definition file from an external source.
	 * 
	 * @param word - The word.
	 * @param file - The word definition file path.
     * @throws RuntimeException if anything goes wrong.
	 */
    public static void fetchWordDefinitionFile(String word, Path file) {
		try {
			Files.createDirectories(file.getParent());
			URL url = new URL(FD_URI + word);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", WEB_USER_AGENTS);
			try (InputStream fromURL = connection.getInputStream();
        		 OutputStream toFile = Files.newOutputStream(file)) 
			{
				IOUtils.copy(fromURL, toFile);
			} 
		} catch (IOException e) {
            try {
                Files.deleteIfExists(file);
            } catch (IOException e1) {
                // Ignore.
            }
            throw new IllegalStateException(e);
		}
	}

    /**
     * Fetches a word definition file from an external source if it is not found locally.
     * Reports on the success or failure of this operation.
     * 
     * @param word - The word.
     * @return The report summary.
     */
    public static FetchSummary fetchWordDefinitionFile(String word) {
        try {
            Path file = FDUtils.getWordDefinitionFilePath(word);
            if (Files.exists(file))
                return FetchSummary.fileFound(word);
            fetchWordDefinitionFile(word, file);
            return FetchSummary.fileFetched(word);
        } catch (RuntimeException e) {
            return FetchSummary.fileNotFetched(word, e.getMessage());
        }
    }

    /**
     * Fetches a word definition file for each word if the file is not found locally.
     * Reports on the success or failure of each fetch operation.
     * 
     * @param words - The array of words.
     * @return The report summary.
     */
    public static FetchSummary[] fetchWordDefinitionFiles(String... words) {
        FetchSummary[] result = new FetchSummary[words.length];
        int i = 0;
        for (String word : words) {
            result[i++] = fetchWordDefinitionFile(word);
        }
        return result;
    }

    /**
     * Performs a crude parse of a word definition file into multiple unstructured data elements.
     * 
     * @param file - The word definition file.
     * @param consumer - A consumer of multiple unstructured data.
     */
    public static void parseWordDefinitionFile(Path file, Consumer<UnstructuredData> consumer) {
        try (InputStream is = Files.newInputStream(file)) {
            SAXParserImpl parser = SAXParserImpl.newInstance(null);
            DefaultHandler handler = new SectionsHandler(consumer);
            parser.parse(is, handler);
        } catch (IOException | SAXException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Performs a crude parse of a word definition file into a list of unstructured data elements.
     * 
     * @param file - The word definition file.
     */
    public static List<UnstructuredData> parseWordDefinitionFile(Path file) {
    	List<UnstructuredData> output = new LinkedList<>();
    	parseWordDefinitionFile(file, output::add);
    	return output;
    }

    /**
     * Parses the definition file of each supplied word (if found).
     * 
     * @param words - The array of words.
     * @return A single unstructured representation of the resulting definition parses.
     */
    public static UnstructuredData parseWordDefinitionFiles(String... words) {
        UnstructuredData parses = UnstructuredData.create();
        for (String word : words) {
            try {
                Path file = getWordDefinitionFilePath(word);
                if (!Files.exists(file)) continue;
                List<UnstructuredData> output = FDUtils.parseWordDefinitionFile(file);
                parses.getMap().put(word, output);
            } catch(RuntimeException e) {
                // Ignore this word.
            }
        }
        return parses;
    }

}
