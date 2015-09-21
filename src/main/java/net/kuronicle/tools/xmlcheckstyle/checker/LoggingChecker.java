package net.kuronicle.tools.xmlcheckstyle.checker;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import lombok.extern.apachecommons.CommonsLog;
import net.kuronicle.tools.xmlcheckstyle.CheckError;

@CommonsLog
public class LoggingChecker implements Checker {

    public CheckError startElement(String xmlPath, StartElement startElement) {
        log.debug("Element start. element="
                + startElement.getName().getLocalPart());
        log.debug("xmlPath=" + xmlPath);
        return null;
    }

    public CheckError endElement(String xmlPath, EndElement endElement) {
        log.debug("Element end. element=" + endElement.getName().getLocalPart());
        return null;
    }

    public CheckError characters(String xmlPath, Characters characters) {
        return null;
    }

    public CheckError startDocument(XMLEvent event) {
        log.debug("Start xml document.");
        return null;
    }

    public CheckError endDocument(XMLEvent event) {
        log.debug("End xml document.");
        return null;
    }

}
