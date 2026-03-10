package one.microstream.domain.postgres;

import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Serdeable
@Entity
@Cacheable(true)
@Table(name = "books", schema = "public")
public class PostBook
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer			id;
    @Column(length = 4000)
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private int pages;
    private String publisher;
    private int year;
    @Column(length = 4000)
    private String description;
    private String language;
    private double price;

    public PostBook() {
    }

    public PostBook(String title, String author, String genre, String isbn, int pages,
                    String publisher, int year, String description, String language,
                    double price) {

        this.title = title;
        this.author = author;
        this.genre = genre;
        this.isbn = isbn;
        this.pages = pages;
        this.publisher = publisher;
        this.year = year;
        this.description = description;
        this.language = language;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
