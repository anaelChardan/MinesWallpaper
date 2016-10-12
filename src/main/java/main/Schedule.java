package main;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class Schedule {
    private Calendar cal;
    private ArrayList<Lesson> cours = new ArrayList<Lesson>();

    public Schedule(Calendar cal) {
        this.cal = cal;
        populate();
    }

    public void populate() {
        for (Iterator i = cal.getComponents().iterator(); i.hasNext();) {
            Component component = (Component) i.next();

            if (component.getName().equals("VEVENT")) {
                cours.add(new Lesson(component));
            }
        }
    }

    public Lesson getCurrentCours() {
        Optional<Lesson> lesson = cours.stream().filter(e -> e.isCoursIsDuring(new Date())).findFirst();

        return lesson.isPresent() ? lesson.get() : null;
    }

    public List<String> getTeachers() {
        return cours
                .stream()
                .map(Lesson::getTeacher)
                .map(e -> e.contains(",") ? e.split(",")[0] : e)
                .filter(e -> !("".equals(e)))
                .distinct()
                .collect(Collectors.toList())
            ;
    }

    public List<Lesson> getTodayLesson()
    {
        return cours.stream().filter(Lesson::isToday).collect(Collectors.toList());
    }

    public List<String> getTeachersCleaned()
    {
        return cours.stream().map(e -> e.getTeacherNormalized()).distinct().filter(e -> !("".equals(e))).collect(Collectors.toList());
    }
}
