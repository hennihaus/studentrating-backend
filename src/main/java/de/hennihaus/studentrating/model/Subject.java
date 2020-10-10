package de.hennihaus.studentrating.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Subject implements Serializable {

    @Id
    private ObjectId id;
    private int semester;
    private String name;
    private LocalDate from;
    private LocalDate to;
    private List<Review> reviews;

    public Subject(SubjectWrapper subject) {
        id = subject.getId();
        semester = subject.getSemester();
        name = subject.getName();
        from = subject.getFrom();
        to = subject.getTo();
        reviews = subject.getReviews();
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class SubjectWrapper {

        private ObjectId id;
        private int semester;
        private String name;
        private LocalDate from;
        private LocalDate to;
        private List<Review> reviews;

        public SubjectWrapper(ObjectId id) {
            this.id = id;
        }

        public Subject wrap() {
            return new Subject(this);
        }
    }
}
