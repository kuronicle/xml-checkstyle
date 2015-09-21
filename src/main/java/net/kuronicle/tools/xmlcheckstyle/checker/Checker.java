package net.kuronicle.tools.xmlcheckstyle.checker;

import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import net.kuronicle.tools.xmlcheckstyle.CheckError;

public interface Checker {

    public CheckError startElement(String xmlPath, StartElement startElement);

    public CheckError endElement(String xmlPath, EndElement endElement);

    public CheckError characters(String xmlPath, Characters characters);

    public CheckError startDocument(XMLEvent event);

    public CheckError endDocument(XMLEvent event);

}
