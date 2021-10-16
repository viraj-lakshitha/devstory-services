package lk.devstory.devstory.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;

/**
 * Resource Already Exists Exceptions
 * */

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends DevStoryException {

    public ResourceAlreadyExistsException(String code, String identifier) {
        super(HttpStatus.CONFLICT, code, Collections.singletonMap("value", identifier));
    }
}
