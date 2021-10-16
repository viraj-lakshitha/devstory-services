package lk.devstory.devstory.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;

/**
 * Bad Request Exceptions
 * */

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends DevStoryException {

    public BadRequestException(String code) {
        this(code, null);
    }

    public BadRequestException(String code, String field) {
        super(HttpStatus.BAD_REQUEST, code, Collections.singletonMap("field", field));
    }

}
