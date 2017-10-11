package gaj.apps.text;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import gaj.text.tokenisation.SequenceTextSpan;
import gaj.text.tokenisation.Tokeniser;
import gaj.text.tokenisation.TokeniserFactory;


@Controller
@RequestMapping("/api/text")
public class AppController {

    @GetMapping(value = "/tokenise/{text}", produces = "application/json")
    public @ResponseBody String tokeniseText(@PathVariable String text) {
        Tokeniser tokeniser = TokeniserFactory.getTokeniser();
        SequenceTextSpan output = tokeniser.tokenise(text);
		return output.toString();
	}

}
