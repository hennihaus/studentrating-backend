package de.hennihaus.studentrating.service;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import de.hennihaus.studentrating.model.*;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static de.hennihaus.studentrating.util.TimeUtil.*;

@Service
public class GenerateRandomProfsServiceImpl implements GenerateRandomProfsService {

    private static final String LANGUAGE = "de";

    @Override
    public List<Professor> generateRandomProfs() throws ParseException {
        List<Professor> professors = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            professors.add(generateRandomProf());
        }
        return professors;
    }

    private Professor generateRandomProf() throws ParseException {
        Faker faker = new Faker(new Locale(LANGUAGE));
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("de"), new RandomService());
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        LocalDate jobStart = convertToLocalDate(faker.date().past(
                7305,
                TimeUnit.DAYS,
                new SimpleDateFormat("yyyy-MM-dd").parse("2020-05-20")
        ));
        return new Professor.ProfessorWrapper(new ObjectId())
                .setTitle(faker.options().option(Title.class))
                .setFirstName(firstName)
                .setLastName(lastName)
                .setFaculty(faker.options().option(Faculty.class))
                .setJobStart(jobStart)
                .setEmail(firstName.toLowerCase().concat(".").concat(lastName.toLowerCase()).concat("@hs-furtwangen.de"))
                .setPhoneNumber(Long.parseLong(fakeValuesService.regexify("(07723|07720|07461){1}[0-9]{7}")))
                .setTalkingTimes(generateRandomTalkingTimes())
                .setSubjects(generateRandomSubjects(jobStart))
                .setThumbnails(generateThumbnails())
                .wrap();
    }

    private LinkedList<TalkingTime> generateRandomTalkingTimes() throws ParseException {
        LinkedList<TalkingTime> talkingTimes = new LinkedList<>();
        TalkingTime talkingTime = generateRandomTalkingTime();
        talkingTimes.add(talkingTime);
        return talkingTimes;
    }

    private TalkingTime generateRandomTalkingTime() throws ParseException {
        Faker faker = new Faker(new Locale(LANGUAGE));
        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("de"), new RandomService());
        Date startTime = faker.date().past(7, TimeUnit.HOURS, new SimpleDateFormat("HH:mm").parse("16:00"));
        return new TalkingTime.TalkingTimeWrapper()
                .setCampus(faker.options().option(Campus.class))
                .setRoom(fakeValuesService.regexify("[A-I]{1}")
                        .concat(fakeValuesService.regexify("[0-3]{1}"))
                        .concat(".")
                        .concat(fakeValuesService.regexify("[0-1]{1}"))
                        .concat(fakeValuesService.regexify("[0-9]{1}"))
                )
                .setDay(faker.options().option(Day.class))
                .setStartTime(convertToLocalDateTime(startTime))
                .setEndTime(convertToLocalDateTime(faker.date().future(1, TimeUnit.HOURS, startTime)))
                .wrap();
    }

    private List<Subject> generateRandomSubjects(LocalDate startJobTime) {
        List<Subject> subjects = new ArrayList<>();
        Faker faker = new Faker(new Locale(LANGUAGE));
        int border = faker.number().numberBetween(1, 4);
        for (int i = 0; i < border; i++) {
            subjects.add(generateRandomSubject(startJobTime));
        }
        return subjects;
    }

    private Subject generateRandomSubject(LocalDate startJobTime) {
        Faker faker = new Faker(new Locale(LANGUAGE));
        return new Subject.SubjectWrapper(new ObjectId())
                .setSemester(faker.number().numberBetween(1, 7))
                .setName("Einführung in die ".concat(faker.programmingLanguage().name()).concat(" Programmierung"))
                .setFrom(startJobTime)
                .setReviews(generateReviews(startJobTime))
                .wrap();
    }

    private List<Review> generateReviews(LocalDate startJobTime) {
        List<Review> reviews = new ArrayList<>();
        Faker faker = new Faker(new Locale(LANGUAGE));
        int countedReviews = faker.number().numberBetween(0, 500);
        for (int i = 0; i < countedReviews; i++) {
            reviews.add(generateReview(startJobTime));
        }
        return reviews;
    }

    private Review generateReview(LocalDate subjectStartTime) {
        Faker faker = new Faker(new Locale(LANGUAGE));
        return new Review.ReviewWrapper(new ObjectId())
                .setRating(faker.number().numberBetween(0, 10))
                .setTitle(faker.lorem().characters(25, 50, true, false))
                .setText(faker.lorem().characters(500, 1000, true, false))
                .setPublished(convertToLocalDate(faker.date().future(
                        (int) ChronoUnit.DAYS.between(subjectStartTime, LocalDate.now()),
                        TimeUnit.DAYS,
                        convertToDate(subjectStartTime))))
                .wrap();
    }

    private List<Thumbnail> generateThumbnails() {
        return List.of(
                generateThumbnail()
        );
    }

    private Thumbnail generateThumbnail() {
        Faker faker = new Faker(new Locale(LANGUAGE));
        return new Thumbnail.ThumbnailWrapper()
                .setUrl(faker.internet().avatar())
                .wrap();
    }
}
