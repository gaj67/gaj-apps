package gaj.apps.text.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller("/api/fd")
public class FDController {

	private static final String BASE_DICT_DIR = "data/fd";
	private static final String BASE_DICT_FILE_TYPE = ".htm";

	@GetMapping(value = "/file/{word}", produces="text/html")
	public @ResponseBody String serve(@PathVariable String word) {
		Path file = getWordFilePath(word);
		if (file == null || !Files.exists(file)) {
			throw new ResourceNotFoundException();
		}
		return getWordFileAsString(file);
	}

	private String getWordFileAsString(Path file) {
		try (InputStream is = Files.newInputStream(file)) {
			return IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new InternalServerErrorException();
		}
	}

	private /*@Nullable*/ Path getWordFilePath(String word) {
		String lword = word.toLowerCase();
		final int length = lword.length();
		if (length >= 3) {
			return Paths.get(BASE_DICT_DIR, lword.substring(0, 1), 
					lword.substring(0, 2), lword.substring(0, 3), lword + BASE_DICT_FILE_TYPE);
		} else if (length == 2) {
			return Paths.get(BASE_DICT_DIR, lword.substring(0, 1), 
					lword.substring(0, 2), lword + BASE_DICT_FILE_TYPE);
		} else if (length == 1) {
			return Paths.get(BASE_DICT_DIR, lword.substring(0, 1), lword + BASE_DICT_FILE_TYPE);
		}
		return null;
	}
	
}
