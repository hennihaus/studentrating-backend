package de.hennihaus.studentrating.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.hennihaus.studentrating.model.Professor;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.hennihaus.studentrating.util.FileUtils.getResourceFileAsString;

@Service
public class ReadProfessorJsonServiceImpl implements ReadProfessorJsonService {

    @Override
    public List<Professor> getProfs() throws JsonProcessingException {
        String jsonString = getResourceFileAsString("json/professors.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, Professor.class));
    }
}
