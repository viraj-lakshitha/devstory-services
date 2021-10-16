package lk.devstory.devstory.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Permission Exceptions
 * */

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PermissionException extends DevStoryException {

    public PermissionException() {
        super(HttpStatus.FORBIDDEN, "permission.denied");
    }
}
