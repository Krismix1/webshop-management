package dk.cristi.app.webshop.management.controllers.http_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public class Http422Exception extends RuntimeException {
    public Http422Exception() {
        super("Unprocessable entity");
    }

    public Http422Exception(String s) {
        super(s);
    }
}
