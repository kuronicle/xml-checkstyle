package net.kuronicle.tools.xmlcheckstyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import net.kuronicle.tools.xmlcheckstyle.checker.Checker;

import com.bea.xml.stream.EventFactory;

@CommonsLog
public class XmlCheckStyle {

    String dummyVersion = "6.9";

    String targetDirPath = "./src/test/resources";

    String targetFilePattern = "^.*\\.xml$";

    @Setter
    List<Checker> checkerList = new ArrayList<Checker>();

    public Map<String, List<CheckError>> check() throws Exception {
        Map<String, List<CheckError>> errorMap = new HashMap<String, List<CheckError>>();

        List<String> targetFileList = listTargetFiles(this.targetDirPath);

        for (String targetFile : targetFileList) {
            List<CheckError> checkErrorList = checkFile(targetFile);
            if (checkErrorList != null && !checkErrorList.isEmpty()) {
                errorMap.put(targetFile, checkErrorList);
            }
        }

        return errorMap;
    }

    public void outputResult(Map<String, List<CheckError>> errorMap)
            throws Exception {

        XMLOutputFactory outFactory = XMLOutputFactory.newInstance();

        StringWriter stringWriter = new StringWriter();
        XMLEventWriter writer = outFactory.createXMLEventWriter(stringWriter);
        XMLEventFactory eventFactory = EventFactory.newInstance();

        writer.add(eventFactory.createStartDocument());
        writer.add(eventFactory.createStartElement("", "", "checkstyle"));
        writer.add(eventFactory.createAttribute("version", dummyVersion));

        for (Entry<String, List<CheckError>> entry : errorMap.entrySet()) {
            String fileName = entry.getKey();
            List<CheckError> errorList = entry.getValue();

            writer.add(eventFactory.createStartElement("", "", "file"));
            writer.add(eventFactory.createAttribute("name", fileName));

            for (CheckError error : errorList) {
                writer.add(eventFactory.createStartElement("", "", "error"));
                writer.add(eventFactory.createAttribute("line", error.getLine()
                        .toString()));
                writer.add(eventFactory.createAttribute("severty", error
                        .getSeverity().getValue()));
                writer.add(eventFactory.createAttribute("message",
                        error.getMessage()));
                writer.add(eventFactory.createAttribute("source",
                        error.getSource()));
                writer.add(eventFactory.createEndElement("", "", "error"));
            }
            writer.add(eventFactory.createEndElement("", "", "file"));
        }

        writer.add(eventFactory.createEndElement("", "", "checkstyle"));
        writer.add(eventFactory.createEndDocument());
        writer.close();

        System.out.println(stringWriter.toString());
    }

    private List<CheckError> checkFile(String targetFile) throws Exception {
        log.debug("Start cheking. file=" + targetFile);

        List<CheckError> erros = new ArrayList<CheckError>();

        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = inputFactory
                .createXMLEventReader(new FileInputStream(new File(targetFile)));

        String xmlPath = "";
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            if (event.isStartDocument()) {
                for (Checker checker : checkerList) {
                    CheckError error = checker.startDocument(event);
                    if(error!= null) {
                        log.info("Detect error. error=" + error);
                        erros.add(error);
                    }
                }
            } else if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                xmlPath += "/" + startElement.getName().getLocalPart();
                for (Checker checker : checkerList) {
                    CheckError error = checker.startElement(xmlPath, startElement);
                    if(error!= null) {
                        log.info("Detect error. error=" + error);
                        erros.add(error);
                    }
                }
            } else if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                xmlPath = xmlPath.substring(0, xmlPath.lastIndexOf("/"));
                for (Checker checker : checkerList) {
                    CheckError error = checker.endElement(xmlPath, endElement);
                    if(error!= null) {
                        log.info("Detect error. error=" + error);
                        erros.add(error);
                    }
                }
            } else if (event.isCharacters()) {
                Characters characters = event.asCharacters();
                for (Checker checker : checkerList) {
                    CheckError error = checker.characters(xmlPath, characters);
                    if(error!= null) {
                        log.info("Detect error. error=" + error);
                        erros.add(error);
                    }
                }
            } else if (event.isEndDocument()) {
                for (Checker checker : checkerList) {
                    CheckError error = checker.endDocument(event);
                    if(error!= null) {
                        log.info("Detect error. error=" + error);
                        erros.add(error);
                    }
                }
            }
        }
        reader.close();

        log.debug("Finish cheking. file=" + targetFile);

        return erros;
    }

    private List<String> listTargetFiles(String targetDir) {
        List<String> filePathList = new ArrayList<String>();

        File dir = new File(targetDir);
        Pattern pattern = Pattern.compile(targetFilePattern);
        findFile(filePathList, dir, pattern);

        return filePathList;
    }

    private void findFile(List<String> filePathList, File dir, Pattern pattern) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                findFile(filePathList, file, pattern);
                continue;
            }
            if (file.isFile()) {
                Matcher matcher = pattern.matcher(file.getName());
                if (matcher.matches()) {
                    filePathList.add(file.getPath());
                }
            }
        }
    }
}
