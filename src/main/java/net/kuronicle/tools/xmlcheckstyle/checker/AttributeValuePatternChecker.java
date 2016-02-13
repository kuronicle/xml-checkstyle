package net.kuronicle.tools.xmlcheckstyle.checker;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import lombok.NonNull;
import lombok.Setter;
import net.kuronicle.tools.xmlcheckstyle.CheckError;
import net.kuronicle.tools.xmlcheckstyle.Severity;

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

    @Setter
    private List<AttributeFilterCondition> attributeFilterConditionList;

    public AttributeValuePatternChecker(String targetXmlPathRegex, String attributeName, String valuePatternRegex, String severity) {
        this.targetXmlPathRegex = targetXmlPathRegex;
        this.attributeName = attributeName;
        this.valuePatternRegex = valuePatternRegex;
        this.severity = Severity.convert(severity);
        targetXmlPathPattern = Pattern.compile(targetXmlPathRegex);
        valuePattern = Pattern.compile(valuePatternRegex);
    }

    public CheckError startElement(String xmlPath, StartElement startElement) {
        if (!isTargetElement(xmlPath, startElement)) {
            return null;
        }

        Attribute targetAttribute = startElement.getAttributeByName(new QName(attributeName));
        if (targetAttribute == null) {
            String message = String.format("The attribute is not fourd. path=%s, attribute=%s", xmlPath, attributeName);
            CheckError error = createCheckError(startElement, message);
            return error;
        }

        String attributeValue = targetAttribute.getValue();

        if (checkAttributeValue(attributeValue)) {
            return null;
        }

        String message = String.format(messageFormat, valuePatternRegex, attributeValue, xmlPath, attributeName);
        CheckError error = createCheckError(startElement, message);

        return error;
    }

    private CheckError createCheckError(StartElement startElement, String message) {
        CheckError error = new CheckError();
        Location location = startElement.getLocation();
        error.setLine(location.getLineNumber());
        error.setColumn(location.getColumnNumber());
        error.setSeverity(severity);
        error.setSource(AttributeValuePatternChecker.class.getName());
        error.setMessage(message);
        return error;
    }

    private boolean checkAttributeValue(String attributeValue) {
        Matcher matcher = valuePattern.matcher(attributeValue);
        return matcher.find();
    }

    private boolean isTargetElement(String xmlPath, StartElement startElement) {
        Matcher matcher = targetXmlPathPattern.matcher(xmlPath);

        if (!matcher.find()) {
            return false;
        }

        if (attributeFilterConditionList != null) {
            for (AttributeFilterCondition condition : attributeFilterConditionList) {
                if (!condition.match(startElement)) {
                    return false;
                }
            }
        }

        return true;
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
