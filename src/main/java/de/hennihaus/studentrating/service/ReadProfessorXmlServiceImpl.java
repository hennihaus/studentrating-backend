package de.hennihaus.studentrating.service;

import de.hennihaus.studentrating.model.*;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.util.*;

import static de.hennihaus.studentrating.util.FileUtils.getResourceFileAsDocument;

@Service
public class ReadProfessorXmlServiceImpl implements ReadProfessorXmlService {

    private static final int FIRST_INDEX = 0;

    @Override
    public List<Professor> getProfs() {
        List<Professor> professors = new ArrayList<>();
        Document document = getResourceFileAsDocument("xml/professors.xml");
        NodeList profs = document.getDocumentElement().getElementsByTagName("professor");
        for (int i = 0; i < profs.getLength(); i++) {
            Element element = (Element) profs.item(i);
            professors.add(getProfFrom(element));
        }
        return professors;
    }

    private Professor getProfFrom(Element prof) {
        return new Professor.ProfessorWrapper(new ObjectId(prof.getAttribute("id")))
                .setFirstName(prof.getAttribute("firstName"))
                .setLastName(prof.getAttribute("lastName"))
                .setThumbnails(getThumbnails((Element) prof.getElementsByTagName("thumbnails").item(FIRST_INDEX)))
                .setSubjects(getSubjects((Element) prof.getElementsByTagName("subjects").item(FIRST_INDEX)))
                .setTalkingTimes(getTalkingTimes((Element) prof.getElementsByTagName("talkingTimes").item(FIRST_INDEX)))
                .setEmail(prof.getAttribute("email"))
                .setJobStart(LocalDate.parse(prof.getAttribute("jobStart")))
                .setFaculty(Faculty.valueOf(prof.getAttribute("faculty")))
                .setTitle(Title.valueOf(prof.getAttribute("title")))
                .setPhoneNumber(Long.parseLong(prof.getAttribute("phoneNumber")))
                .wrap();
    }

    private List<Thumbnail> getThumbnails(Element thumbnailsElement) {
        List<Thumbnail> thumbnails = new ArrayList<>();
        NodeList nodes = thumbnailsElement.getElementsByTagName("thumbnail");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element thumbnail = (Element) nodes.item(i);
            thumbnails.add(getThumbnail(thumbnail));
        }
        return thumbnails;
    }


    private Thumbnail getThumbnail(Element thumbnail) {
        return new Thumbnail.ThumbnailWrapper()
                .setUrl(thumbnail.getAttribute("url"))
                .setTitle(Optional.ofNullable(thumbnail.getAttribute("title")).orElse(null))
                .wrap();
    }

    private List<Subject> getSubjects(Element subjectsElement) {
        List<Subject> subjects = new ArrayList<>();
        NodeList nodes = subjectsElement.getElementsByTagName("subject");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element subject = (Element) nodes.item(i);
            subjects.add(getSubject(subject));
        }
        return subjects;
    }

    private Subject getSubject(Element subject) {
        return new Subject.SubjectWrapper(new ObjectId(subject.getAttribute("id")))
                .setReviews(getReviews((Element) subject.getElementsByTagName("reviews").item(FIRST_INDEX)))
                .setFrom(LocalDate.parse(subject.getAttribute("from")))
                .setName(subject.getAttribute("name"))
                .setSemester(Integer.parseInt(subject.getAttribute("semester")))
                .wrap();
    }

    private List<Review> getReviews(Element reviewsElement) {
        List<Review> reviews = new ArrayList<>();
        NodeList nodes = reviewsElement.getElementsByTagName("review");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element review = (Element) nodes.item(i);
            reviews.add(getReview(review));
        }
        return reviews;
    }

    private Review getReview(Element review) {
        return new Review.ReviewWrapper(new ObjectId(review.getAttribute("id")))
                .setTitle(review.getAttribute("title"))
                .setPublished(LocalDate.parse(review.getAttribute("published")))
                .setText(review.getTextContent())
                .setRating(Double.parseDouble(review.getAttribute("rating")))
                .wrap();
    }

    private LinkedList<TalkingTime> getTalkingTimes(Element talkingTimesElement) {
        LinkedList<TalkingTime> talkingTimes = new LinkedList<>();
        NodeList nodes = talkingTimesElement.getElementsByTagName("talkingTime");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element talkingTime = (Element) nodes.item(i);
            talkingTimes.add(getTalkingTime(talkingTime));
        }
        return talkingTimes;
    }

    private TalkingTime getTalkingTime(Element talkingTime) {
        String startTime = talkingTime.getAttribute("startTime");
        String endTime = talkingTime.getAttribute("endTime");
        return new TalkingTime.TalkingTimeWrapper()
                .setDay(Day.valueOf(talkingTime.getAttribute("day")))
                .setCampus(Campus.valueOf(talkingTime.getAttribute("campus")))
                .setRoom(talkingTime.getAttribute("room"))
                .setStartTime(LocalDate.now().atTime(
                        Integer.parseInt(StringUtils.substringBefore(startTime, ":")),
                        Integer.parseInt(StringUtils.substringAfter(startTime, ":"))
                ))
                .setEndTime(LocalDate.now().atTime(
                        Integer.parseInt(StringUtils.substringBefore(endTime, ":")),
                        Integer.parseInt(StringUtils.substringAfter(endTime, ":"))
                ))
                .wrap();
    }

}
