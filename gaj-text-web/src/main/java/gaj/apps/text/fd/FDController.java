package gaj.apps.text.fd;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import gaj.apps.text.fd.parser.UnstructuredData;


@Controller
@RequestMapping("/api/fd")
public class FDController {

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

    @GetMapping(value = "/gather/{words}", produces="text/html")
    public @ResponseBody String gatherFiles(@PathVariable String words) {
        if (words == null || words.trim().isEmpty()) {
            return "No words were specified";
        }
        FetchSummary[] wordSummaries = FDUtils.fetchWordFiles(words.trim().split("\\s"));
        StringBuilder html = new StringBuilder()
            .append("<html>")
            .append("  <body>")
            .append("    <h3>Gathered words</h3>")
            .append("    <ul>");
        for (FetchSummary wordSummary : wordSummaries) {
            html.append("<li>")
                .append("<b>")
                .append(wordSummary.getWord())
                .append("</b> [")
                .append(wordSummary.getWordCount())
                .append("] - ")
                .append(wordSummary.wasFound() ? "exists" : wordSummary.wasFetched() ? "fetched" : wordSummary.getError())
                .append("</li>");
        }
        html.append("    </ul>")
            .append("  </body>")
            .append("</html>");
        return html.toString();
    }

	@GetMapping(value = "/parse/{word}", produces="application/json")
	public @ResponseBody String parseFile(@PathVariable String word) {
		Path file = FDUtils.getWordFilePath(word);
		if (file == null || !Files.exists(file)) {
			throw new ResourceNotFoundException();
		}
		List<UnstructuredData> output = FDUtils.parseWordFile(file);
		return output.toString();
	}

}
