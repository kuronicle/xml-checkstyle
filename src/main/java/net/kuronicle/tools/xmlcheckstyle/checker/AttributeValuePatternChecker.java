package net.kuronicle.tools.xmlcheckstyle.checker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import lombok.Data;
import lombok.NonNull;
import net.kuronicle.tools.xmlcheckstyle.CheckError;
import net.kuronicle.tools.xmlcheckstyle.Severity;

@Data
public class AttributeValuePatternChecker extends BaseChecker implements Checker {

    @NonNull
    private String targetXmlPathRegex;

    @NonNull
    private String attributeName;

    @NonNull
    private String valuePatternRegex;

    @NonNull
    private Severity severity;

    private Pattern targetXmlPathPattern;

    private Pattern valuePattern;

    private String messageFormat = "The expected attribute value is \"%s\", but \"%s\". path=%s, attribute=%s";

    public AttributeValuePatternChecker(String targetXmlPathRegex,
            String attributeName, String valuePatternRegex, String severity) {
        this.targetXmlPathRegex = targetXmlPathRegex;
        this.attributeName = attributeName;
        this.valuePatternRegex = valuePatternRegex;
        this.severity = Severity.convert(severity);
        targetXmlPathPattern = Pattern.compile(targetXmlPathRegex);
        valuePattern = Pattern.compile(valuePatternRegex);
    }

    public CheckError startElement(String xmlPath, StartElement startElement) {
        if (!isTargetElement(xmlPath)) {
            return null;
        }

        Attribute targetAttribute = startElement.getAttributeByName(new QName(
                attributeName));
        if (targetAttribute == null) {
            return null;
        }

        String attributeValue = targetAttribute.getValue();

        if (checkAttributeValue(attributeValue)) {
            return null;
        }

        Location location = startElement.getLocation();

        CheckError error = new CheckError();
        error.setLine(location.getLineNumber());
        error.setColumn(location.getColumnNumber());
        error.setSeverity(severity);
        error.setSource(AttributeValuePatternChecker.class.getName());
        error.setMessage(String.format(messageFormat, valuePatternRegex,
               attributeValue, xmlPath, attributeName));

        return error;
    }

    private boolean checkAttributeValue(String attributeValue) {
        Matcher matcher = valuePattern.matcher(attributeValue);
        return matcher.find();
    }

    private boolean isTargetElement(String xmlPath) {
        Matcher matcher = targetXmlPathPattern.matcher(xmlPath);
        return matcher.find();
    }

    public CheckError endElement(String xmlPath, EndElement endElement) {
        return null;
    }

    public CheckError characters(String xmlPath, Characters characters) {
        return null;
    }

    public CheckError startDocument(XMLEvent event) {
        return null;
    }

    public CheckError endDocument(XMLEvent event) {
        return null;
    }

}
