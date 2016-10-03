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

import org.apache.commons.io.IOUtils;

/*package-private*/ class FDUtils {

	private static final String WEB_USER_AGENTS = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";
	private static final String DICT_DIR = "data/fd";
	private static final String DICT_FILE_TYPE = ".htm";
	private static final String FD_URI = "http://www.thefreedictionary.com/";

	/*package-private*/ static /*@Nullable*/ Path getWordFilePath(String word) {
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
		return null;
	}
	
	/*package-private*/ static /*@Nullable*/ String readWordFileAsString(Path file) {
		try (InputStream is = Files.newInputStream(file)) {
			return IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			return null;
		}
	}

	/*package-private*/ static boolean fetchWordFile(String word, Path file) {
		try {
			Files.createDirectories(file.getParent());
			URL url = new URL(FD_URI + word);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", WEB_USER_AGENTS);
			try (InputStream fromURL = connection.getInputStream();
        		 OutputStream toFile = Files.newOutputStream(file)) 
			{
				IOUtils.copy(fromURL, toFile);
				return true;
			} 
		} catch (IOException e) {
			e.printStackTrace(System.err);
			return false;
		}
	}

}
