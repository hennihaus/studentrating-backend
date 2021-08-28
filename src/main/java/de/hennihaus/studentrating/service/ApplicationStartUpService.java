package de.hennihaus.studentrating.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.hennihaus.studentrating.model.Professor;
import de.hennihaus.studentrating.repository.ProfessorRepository;
import de.hennihaus.studentrating.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ApplicationStartUpService implements ApplicationListener<ApplicationReadyEvent> {

    private final ProfessorRepository professorRepository;
        private final SubjectRepository subjectRepository;
        private final GenerateRandomProfsService generateRandomProfsService;
        private final ReadProfessorJsonService readProfessorJsonService;
        private final ReadProfessorXmlService readProfessorXmlService;

    @Autowired
    public ApplicationStartUpService(ProfessorRepository professorRepository, SubjectRepository subjectRepository, GenerateRandomProfsService generateRandomProfsService, ReadProfessorJsonService readProfessorJsonService, ReadProfessorXmlService readProfessorXmlService) {
            this.professorRepository = professorRepository;
            this.subjectRepository = subjectRepository;
            this.generateRandomProfsService = generateRandomProfsService;
            this.readProfessorJsonService = readProfessorJsonService;
        this.readProfessorXmlService = readProfessorXmlService;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        try {
            subjectRepository.deleteAll();
            professorRepository.deleteAll();

            List<Professor> professors = new ArrayList<>();
            professors.addAll(readProfessorXmlService.getProfs());
            professors.addAll(readProfessorJsonService.getProfs());
            professors.addAll(generateRandomProfsService.generateRandomProfs());
            professorRepository
                    .saveAll(professors)
                    .stream()
                    .map(Professor::getSubjects)
                    .forEach(subjectRepository::saveAll);

            Map<ObjectId, Professor> professorMap = professors.stream().collect(Collectors.toMap(Professor::getId, prof -> prof));
            log.info("Professor: " + professorMap.get(new ObjectId("5edf1e8f1c4dc06c850bc412")));

        } catch (ParseException e) {
            log.error("Error while loading initial data into MongoDB: ", e);
        } catch (JsonProcessingException e) {
            log.error("Error while parsing json to pojo: ", e);
        }
    }
}
