package de.hennihaus.studentrating.repository;

import de.hennihaus.studentrating.model.Professor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProfessorRepository extends MongoRepository<Professor, ObjectId> {

    /**
     * e.g. db.professor.find({$text: {$search: "Siestrup Andreas"}})
     * @param textCriteria first and last Name
     * @return all valid profs
     */
    List<Professor> findAllBy(TextCriteria textCriteria);

    /**
     * e.g. db.professor.find({"subjects.$id": ObjectId("5eddf716ceb1501fa39286d7")})
     * @param id
     * @return all valid profs
     */
    @Query(value = "{ 'subjects.$id': ?0 }")
    List<Professor> findAllBySubjectsId(ObjectId id);
}
