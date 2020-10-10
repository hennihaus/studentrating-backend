package de.hennihaus.studentrating.repository;

import de.hennihaus.studentrating.model.Review;
import de.hennihaus.studentrating.model.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class SubjectRepositoryImpl implements SubjectRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SubjectRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Review addReviewToSubject(Review.ReviewWrapper reviewWrapper) {
        Review review = new Review(reviewWrapper);
        mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(reviewWrapper.getId())),
                new Update().push("reviews", review),
                Subject.class
        );
        return review;
    }
}
