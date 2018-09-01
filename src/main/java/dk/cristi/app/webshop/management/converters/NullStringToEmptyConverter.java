package dk.cristi.app.webshop.management.converters;

import org.apache.commons.lang.StringUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class NullStringToEmptyConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String string) {
        // Use defaultIfEmpty to preserve Strings consisting only of whitespaces
        return StringUtils.defaultIfBlank(string, "");
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return StringUtils.defaultIfEmpty(dbData, null);
    }
}
