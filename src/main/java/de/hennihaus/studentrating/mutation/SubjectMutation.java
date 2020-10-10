package de.hennihaus.studentrating.mutation;

import de.hennihaus.studentrating.model.Review;
import de.hennihaus.studentrating.repository.SubjectRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubjectMutation implements GraphQLMutationResolver {

    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectMutation(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public Review createReview(Review.ReviewWrapper review) {
        return subjectRepository.addReviewToSubject(review);
    }

}
