package dk.cristi.app.webshop.management.helpers;

import org.springframework.web.util.UriComponentsBuilder;

public class UriEncoder {
    public static String encode(String value) {
        return UriComponentsBuilder.fromUriString("{value}").buildAndExpand(value).encode().toUriString();
    }
}
