package de.hennihaus.studentrating.resolver;

import de.hennihaus.studentrating.model.Faculty;
import de.hennihaus.studentrating.model.Professor;
import de.hennihaus.studentrating.model.Review;
import de.hennihaus.studentrating.repository.ProfessorRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ProfessorResolver implements GraphQLQueryResolver {

    public enum SortDirection {
        ASC,
        DESC
    }

    private final ProfessorRepository professorRepository;

    @Autowired
    public ProfessorResolver(ProfessorRepository professorRepository) {
        this.professorRepository = professorRepository;
    }

    /**
     * e.g. db.professor.find()
     * @param sortDirection
     * @return all profs
     */
    public List<Professor> getAllProfs(SortDirection sortDirection) {
        if (sortDirection.equals(SortDirection.ASC)) {
            return professorRepository
                    .findAll()
                    .stream()
                    .sorted(Comparator.comparing(Professor::getLastName))
                    .collect(Collectors.toList());
        }
        return professorRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(Professor::getLastName).reversed())
                .collect(Collectors.toList());
    }

    public List<Professor> getAllProfsSearch(String searchTerm) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage().matching(searchTerm);
        return professorRepository
                .findAllBy(textCriteria)
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public Professor getProfById(String id) {
        Professor professor = professorRepository
                .findById(new ObjectId(id))
                .orElse(new Professor.ProfessorWrapper(new ObjectId()).wrap());

        double averageRating = professor.getSubjects()
                .stream()
                .flatMap(subject -> subject.getReviews().stream())
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0d);

        professor.setAverageRating(averageRating);

        return professor;
    }

    public Professor getProfBySubjectId(String id) {
        return professorRepository
                .findAllBySubjectsId(new ObjectId(id))
                .stream()
                .findFirst()
                .orElse(new Professor.ProfessorWrapper(new ObjectId()).wrap());
    }

    public List<Professor> getProfsBy(LocalDate jobStartEarlierThan, String lastName, Faculty faculty) {
        Map<Faculty, List<Professor>> professors = professorRepository
                .findAll()
                .stream()
                .filter(professor -> professor.getJobStart().isBefore(jobStartEarlierThan) && professor.getLastName().startsWith(lastName))
                .collect(Collectors.groupingBy(Professor::getFaculty, Collectors.toList()));

        return professors
                .entrySet()
                .stream()
                .filter(facultyProfsEntry -> facultyProfsEntry.getKey().equals(faculty))
                .flatMap(facultyListEntry -> facultyListEntry.getValue().stream())
                .collect(Collectors.toList());
    }
}
