package de.hennihaus.studentrating.resolver;

import de.hennihaus.studentrating.model.Review;
import de.hennihaus.studentrating.model.Subject;
import de.hennihaus.studentrating.repository.SubjectRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubjectResolver implements GraphQLQueryResolver {

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectResolver(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    /**
     * db.subject.find()
     * @return all subjects
     */
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    /**
     * db.subject.find({_id: ObjectId("5eddf716ceb1501fa39286d7")})
     * @param id
     * @return
     */
    public Subject getSubjectById(String id) {
        return subjectRepository
                .findById(new ObjectId(id))
                .orElse(new Subject.SubjectWrapper(new ObjectId()).wrap());
    }

    public Review getReviewById(String id) {
        ObjectId reviewId = new ObjectId(id);
        return subjectRepository.findByReviewsId(reviewId)
                .getReviews()
                .stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElse(new Review.ReviewWrapper(new ObjectId()).wrap());
    }
}
