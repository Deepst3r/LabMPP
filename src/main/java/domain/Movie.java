package domain;

import java.util.function.Predicate;

/**
 * Class representing a movie entity
 */
public class Movie extends BaseEntity {

    public static Predicate<Movie> isNiceMovie() {
        return p -> p.getRating() > 8.0;
    }

    public static Predicate<Movie> isSequel() {
        return p -> p.getTitle().endsWith("2");
    }

    public static Predicate<Movie> isOld() {
        return p -> p.getYear() < 2005;
    }


    private String title;
    private double rating;
    private int year;
    private String genre;

    public Movie(String title, double rating, int year, String genre) {
        super();
        this.title = title;
        this.rating = rating;
        this.year = year;
        this.genre = genre;
    }


    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public int getYear() {
        return year;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return getId() + " " + getTitle() + " (" + getYear() + ") " + getRating() + "/10";
    }
}
