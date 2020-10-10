package de.hennihaus.studentrating.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Review {

    @Id
    private ObjectId id;
    private double rating;
    private String title;
    private String text;
    private LocalDate published;

    public Review(ReviewWrapper review) {
        id = review.getId();
        rating = review.getRating();
        title = review.getTitle();
        text = review.getText();
        Optional.ofNullable(review.getPublished()).ifPresentOrElse(p -> published = p, () -> published = LocalDate.now());
    }

    @Setter
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @Accessors(chain = true)
    public static class ReviewWrapper {

        private ObjectId id;
        private double rating;
        private String title;
        private String text;
        private LocalDate published;

        public ReviewWrapper(ObjectId id) {
            this.id = id;
        }

        public Review wrap() {
            return new Review(this);
        }
    }
}
