package lk.devstory.devstory.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

/**
 * DevStory Custom Exceptions
 * */

@Builder
@Getter
@JsonIgnoreProperties(value = {"suppressed", "localizedMessage", "message", "cause", "stackTrace", "httpStatus"}, ignoreUnknown = true)
public class DevStoryException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String code;
    private final transient Map<String , Object> details;

    // Instantiates Platform Errors
    public DevStoryException() {
        this(HttpStatus.INTERNAL_SERVER_ERROR, "internal.error", null, null);
    }

    // Instantiates New Platform Exception
    public DevStoryException(HttpStatus httpStatus, String code) {
        this(httpStatus, code, null, null);
    }

    // Instantiates New Platform Exception
    public DevStoryException(HttpStatus httpStatus, String code, Map<String, Object> details) {
        this(httpStatus, code, details, null);
    }

    // Instantiate New Platform Exception
    public DevStoryException(HttpStatus httpStatus, String code, Map<String, Object> details, Throwable cause) {
        super(code, cause);
        this.httpStatus = httpStatus;
        this.code = code;
        this.details = details;
    }

}