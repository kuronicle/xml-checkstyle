package net.kuronicle.tools.xmlcheckstyle.checker;

import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;

import lombok.NonNull;

public class AttributeFilterCondition {

    private String attributeName;

    private Pattern attributeValuePattern;

    public AttributeFilterCondition(@NonNull String attributeName, @NonNull String attributeValueRegex) {
        this.attributeName = attributeName;
        attributeValuePattern = Pattern.compile(attributeValueRegex);
    }

    public boolean match(StartElement startElement) {
        Attribute attribute = startElement.getAttributeByName(new QName(attributeName));

        if (attribute == null) {
            return false;
        }

        String attributeValue = attribute.getValue();
        if (attributeValuePattern.matcher(attributeValue).matches()) {
            return true;
        }

        return false;
    }
}
