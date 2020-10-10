package de.hennihaus.studentrating.mutation;

import de.hennihaus.studentrating.model.Professor;
import de.hennihaus.studentrating.model.Subject;
import de.hennihaus.studentrating.repository.ProfessorRepository;
import de.hennihaus.studentrating.repository.SubjectRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfessorMutation implements GraphQLMutationResolver {

    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public ProfessorMutation(ProfessorRepository professorRepository, SubjectRepository subjectRepository) {
        this.professorRepository = professorRepository;
        this.subjectRepository = subjectRepository;
    }

    /**
     * e.g. db.professor.remove({_id:ObjectId("5ec85993ace5c63c8d5972e9")})
     * @param id
     * @return id from deleted prof
     */
    public String deleteProfBy(String id) {
        ObjectId profId = new ObjectId(id);
        professorRepository.findById(profId)
                .map(Professor::getSubjects)
                .ifPresent(subjects -> subjects.stream()
                        .map(Subject::getId)
                        .forEach(subjectRepository::deleteById)
                );
        professorRepository.deleteById(new ObjectId(id));
        return id;
    }
}
