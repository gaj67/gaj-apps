package gaj.apps.text.fd;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import gaj.apps.text.JSONHandler;
import gaj.apps.text.fd.parser.UnstructuredData;


@Controller
@RequestMapping("/api/fd")
public class FDController {

    /**
     * Obtains the local definition file for the given word.
     * 
     * @param word - The queried word.
     * @return The HTML definition file, or an error if the definition file is not found.
     */
	@GetMapping(value = "/file/{word}", produces="text/html")
	public @ResponseBody String getFile(@PathVariable String word) {
		Path file = FDUtils.getWordFilePath(word);
		if (file == null || !Files.exists(file)) {
			throw new ResourceNotFoundException();
		}
		String html = FDUtils.readWordFileAsString(file);
		if (html == null) {
			throw new InternalServerErrorException();
		}
		return html;
	}

    /**
     * Obtains the local definition file for the given word. The definition file is fetched
     * externally if it does not exist locally.
     * 
     * @param word - The queried word.
     * @return The HTML definition file.
     */
	@GetMapping(value = "/fetch/{word}", produces="text/html")
	public @ResponseBody String fetchFile(@PathVariable String word) {
		Path file = FDUtils.getWordFilePath(word);
		if (file == null) {
			throw new ResourceNotFoundException();
		}
		if (!Files.exists(file) && !FDUtils.fetchWordFile(word, file)) {
			throw new ResourceNotFoundException();
		}
		if (!Files.exists(file)) {
			throw new ResourceNotFoundException();
		}
		String html = FDUtils.readWordFileAsString(file);
		if (html == null) {
			throw new InternalServerErrorException();
		}
		return html;
	}

    /**
     * Parses the local definition file for the given word.
     * 
     * @param word - The queried word.
     * @return A JSON summary of the definition file, or an error if the definition file is not found.
     */
    @GetMapping(value = "/parse/{word}", produces="application/json")
    public @ResponseBody String parseFile(@PathVariable String word) {
        Path file = FDUtils.getWordFilePath(word);
        if (file == null || !Files.exists(file)) {
            throw new ResourceNotFoundException();
        }
        List<UnstructuredData> output = FDUtils.parseWordFile(file);
        return output.toString();
    }

	/**
	 * If necessary, fetches the definition file for each word identified in the given text.
	 * 
	 * @param text - The supplied text.
	 * @return The JSON summary of the gathering process.
	 */
    @GetMapping(value = "/gather/{text}", produces = "application/json")
    public @ResponseBody String gatherFiles(@PathVariable String text) {
        if (text == null || text.trim().isEmpty()) {
            return "[]";
        }
        FetchSummary[] wordSummaries = FDUtils.fetchWordFiles(text);
        return JSONHandler.toJSONString(wordSummaries);
    }

}
