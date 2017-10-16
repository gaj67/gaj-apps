package gaj.apps.text.fd;

import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import gaj.apps.text.basic.JSONHandler;
import gaj.apps.text.basic.TextUtils;
import gaj.apps.text.error.ResponseExceptions;
import gaj.apps.text.fd.parser.UnstructuredData;


@Controller
@RequestMapping("/api/fd")
public class FDController {

    private /*@Nullable*/ Path safeGetPath(String word) {
        try {
            return FDUtils.getWordDefinitionFilePath(word);
        } catch (RuntimeException e) {
            return null;
        }
    }

    private String /*@Nullable*/ safeGetContents(Path file) {
        try {
            return FDUtils.readFileAsString(file);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * Obtains the local definition file for the given word.
     * 
     * @param word - The queried word.
     * @return The HTML definition file, or an error if the definition file is not found.
     */
	@GetMapping(value = "/file/{word}", produces="text/html")
    public @ResponseBody String getWordDefinitionFile(@PathVariable String word) {
        Path file = safeGetPath(word);
        if (file == null || !Files.exists(file))
            throw ResponseExceptions.resourceNotFound();
        String html = safeGetContents(file);
        if (html == null)
            throw ResponseExceptions.internalServerError();
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
    public @ResponseBody String fetchWordDefinitionFile(@PathVariable String word) {
        Path file = safeGetPath(word);
        if (file == null)
            throw ResponseExceptions.resourceNotFound();
        if (!Files.exists(file)) {
            FetchSummary summary = FDUtils.fetchWordDefinitionFile(word);
            if (summary.getError() != null || !Files.exists(file))
                throw ResponseExceptions.resourceNotFound();
        }
        String html = safeGetContents(file);
        if (html == null)
            throw ResponseExceptions.internalServerError();
		return html;
	}

	/**
	 * If necessary, fetches the definition file for each word identified in the given text.
	 * 
	 * @param text - The supplied text.
	 * @return The JSON summary of the gathering process.
	 */
    @GetMapping(value = "/gather/{text}", produces = "application/json")
    public @ResponseBody String gatherWordDefinitionFiles(@PathVariable String text) {
        FetchSummary[] output = FDUtils.fetchWordDefinitionFiles(TextUtils.getUniqueWordsFromText(text));
        return JSONHandler.toJSONString(output);
    }

    /**
     * Parses the local definition files for the words in the given text.
     * 
     * @param text - The input text.
     * @return A JSON summary of the definition files.
     */
    @GetMapping(value = "/parse/{text}", produces="application/json")
    public @ResponseBody String parseWordDefinitionFiles(@PathVariable String text) {
        UnstructuredData output = FDUtils.parseWordDefinitionFiles(TextUtils.getUniqueWordsFromText(text));
        return JSONHandler.toJSONString(output);
    }

}
