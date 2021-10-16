package lk.devstory.devstory.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;

/**
 * Resource Not Found Exceptions
 * */

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends DevStoryException {

    public ResourceNotFoundException(String code) {
        this(code, null);
    }

    public ResourceNotFoundException(String code, Long identifier) {
        super(HttpStatus.NOT_FOUND, code, Collections.singletonMap("value", identifier));
    }
}
