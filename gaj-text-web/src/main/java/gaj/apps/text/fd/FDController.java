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
