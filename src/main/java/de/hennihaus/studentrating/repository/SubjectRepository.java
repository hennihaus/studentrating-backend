package de.hennihaus.studentrating.repository;

import de.hennihaus.studentrating.model.Subject;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface SubjectRepository extends MongoRepository<Subject, ObjectId>, SubjectRepositoryCustom {

    /**
     * e.g. db.subject.find({"reviews._id": ObjectId("5eddf737c427e1b747076742")})
     * @param id
     * @return
     */
    @Query(value = "{ 'reviews._id': ?0 }")
    Subject findByReviewsId(ObjectId id);
}
