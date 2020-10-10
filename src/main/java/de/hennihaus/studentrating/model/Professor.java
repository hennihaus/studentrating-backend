package de.hennihaus.studentrating.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Document
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Professor implements Serializable {

    @Id
    private ObjectId id;
    private Title title;
    @TextIndexed
    private String firstName;
    @TextIndexed
    private String lastName;
    private Faculty faculty;
    private LocalDate jobStart;
    @Transient
    @Setter
    private double averageRating;
    private String email;
    private long phoneNumber;
    private LinkedList<TalkingTime> talkingTimes;
    @DBRef
    private List<Subject> subjects;
    private List<Thumbnail> thumbnails;

    public Professor(ProfessorWrapper professor) {
        id = professor.getId();
        title = professor.getTitle();
        firstName = professor.getFirstName();
        lastName = professor.getLastName();
        faculty = professor.getFaculty();
        jobStart = professor.getJobStart();
        email = professor.getEmail();
        phoneNumber = professor.getPhoneNumber();
        talkingTimes = professor.getTalkingTimes();
        subjects = professor.getSubjects();
        thumbnails = professor.getThumbnails();
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class ProfessorWrapper {

        private ObjectId id;
        private Title title;
        private String firstName;
        private String lastName;
        private Faculty faculty;
        private LocalDate jobStart;
        private String email;
        private long phoneNumber;
        private LinkedList<TalkingTime> talkingTimes;
        private List<Subject> subjects;
        private List<Thumbnail> thumbnails;

        public ProfessorWrapper(ObjectId id) {
            this.id = id;
        }

        public Professor wrap() {
            return new Professor(this);
        }
    }
}
