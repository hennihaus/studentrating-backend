package de.hennihaus.studentrating.service;

import de.hennihaus.studentrating.model.Professor;

import java.text.ParseException;
import java.util.List;

public interface GenerateRandomProfsService {
    List<Professor> generateRandomProfs() throws ParseException;
}
