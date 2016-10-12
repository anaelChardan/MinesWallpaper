package main;


import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class Lesson {
    private Component component = null;
    private Date startDate = null;
    private Date endDate = null;
    private String title = "";
    private String teacher = "";


    public Lesson(Component component) {
        this.component = component;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'hhmmss");
//        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

        Property start = component.getProperty("DTSTART");
        try {
            startDate = formatter.parse(start.getValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Property end = component.getProperty("DTEND");
        try {
            endDate = formatter.parse(end.getValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Property summary = component.getProperty("SUMMARY");
        title = summary.getValue();

        Property comment = component.getProperty("COMMENT");
        teacher = comment.getValue();
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getTitle() {
        return title;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getTeacherNormalized()
    {
        String name = teacher.contains(",") ? teacher.split(",")[0] : teacher;

        return Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").replaceAll(" ", "_");
    }

    public boolean isCoursIsDuring(Date d) {
        if (startDate != null && endDate != null)
            return !(d.before(startDate) || d.after(endDate));
        return false;
    }

    public boolean isToday()
    {
        if (startDate != null && endDate != null)
        {
            return isSameDay(startDate, Calendar.getInstance().getTime());
        }
        return false;
    }

    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * <p>Checks if two calendars represent the same day ignoring time.</p>
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    public String toString()
    {
        return this.title + " " + this.getTeacherNormalized() + ": " + startDate + " -> " + endDate;
    }
}

