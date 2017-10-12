package gaj.apps.text.error;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.NOT_FOUND)
/*package-private*/ class ResourceNotFoundException extends RuntimeException {
}
