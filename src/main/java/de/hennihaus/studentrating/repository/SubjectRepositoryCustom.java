package de.hennihaus.studentrating.repository;

import de.hennihaus.studentrating.model.Review;

public interface SubjectRepositoryCustom {

    /**
     * e.g. db.subject.update(
     *  { _id: ObjectId("5eddf716ceb1501fa39286d7")},
     *  {$push: { reviews: { rating: 8, title: "Gut", text: "Sehr Gut", published: ISODate("2020-06-08T22:00:00Z"), _id: ObjectId("5edf3ccadab1a059801f93dc") }}}
     * )
     * @param reviewWrapper
     * @return added review
     */
    Review addReviewToSubject(Review.ReviewWrapper reviewWrapper);
}
