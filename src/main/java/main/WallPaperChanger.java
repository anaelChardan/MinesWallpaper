package main;

import java.io.*;
import java.net.URL;
import java.util.List;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

public class WallPaperChanger {
    private static final String icsURL = "http://web.emn.fr/x-sic/oasis-sigs/get-ics.php?id=achard15&check=4c7e46675c31e6da30b2e71f8feda9d3";
    protected static Schedule schedule = null;

    public static void main(String[] args) throws IOException, ParserException {
        InputStream stream = null;

        try {
            stream = new URL(icsURL).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Une erreur s'est produite lors du chargement du calendrier");
            System.out.println("URL : " + icsURL);
        }

        CalendarBuilder builder = new CalendarBuilder();

        Calendar calendar = null;
        try {
            calendar = builder.build(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserException e) {
            System.out.println("Erreur du parsage du calendier");
        }

        schedule = new Schedule(calendar);


        if (args.length > 0 && args[0].equals("--profs")) {
            List<String> prof = schedule.getTeachersCleaned();
            System.out.println("Mes profs : ");
            prof.forEach(System.out::println);

            return;
        }

        if (args.length > 0 && args[0].equals("--today")) {
            List<Lesson> lessons = schedule.getTodayLesson();
            System.out.println("Today : ");
            lessons.forEach(System.out::println);

            return;
        }

        Lesson cours =  schedule.getCurrentCours();
        String photo_path = "wallpapers/DEFAULT.jpg";

        if (null != cours)
        {
            if (getFile("wallpapers/"+cours.getTeacherNormalized()+".jpg").exists())
            {
                photo_path = "wallpapers/"+ cours.getTeacherNormalized()+".jpg";
            }
        }

        try {
            if (!getFile(photo_path).exists())
            {
                System.out.println("PHOTO not Founs");
            }
            setWallpaper(getFile(photo_path));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static File getFile(String path)
    {
        ClassLoader classLoader = schedule.getClass().getClassLoader();
        return new File(classLoader.getResource(path).getFile());
    }

    public static void setWallpaper(File file)
            throws Exception {
            Runtime.getRuntime().exec(new String[] { "osascript", "-e", "tell application \"Finder\" to set desktop picture to POSIX file \""+file.getAbsolutePath()+"\""});
    }

}
