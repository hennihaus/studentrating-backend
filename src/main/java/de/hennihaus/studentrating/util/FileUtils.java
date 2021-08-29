package de.hennihaus.studentrating.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class FileUtils {

    /**
     * Reads given resource file as a string.
     *
     * @param fileName the path to the resource file
     * @return the file's contents or null if the file could not be opened
     */
    public static String getResourceFileAsString(String fileName) {
        try {
            InputStream inputStream = new ClassPathResource(fileName).getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            log.error("Error while reading ".concat(fileName).concat(": "), e);
            return null;
        }
    }

    public static Document getResourceFileAsDocument(String fileName) {
        try {
            InputStream inputStream = new ClassPathResource(fileName).getInputStream();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(inputStream);
        } catch (Exception e) {
            log.error("Error while reading ".concat(fileName).concat(": "), e);
            return null;
        }
    }
}
