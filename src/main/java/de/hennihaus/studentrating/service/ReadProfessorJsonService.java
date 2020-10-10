package de.hennihaus.studentrating.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.hennihaus.studentrating.model.Professor;

import java.util.List;

public interface ReadProfessorJsonService {
    List<Professor> getProfs() throws JsonProcessingException;
}
