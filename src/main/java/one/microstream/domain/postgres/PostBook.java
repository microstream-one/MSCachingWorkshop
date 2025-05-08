package one.microstream.domain.postgres;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

@Serdeable
@Entity
@Cacheable(true)
@Table(name = "books", schema = "public")
public class PostBook
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer			id;
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private int pages;

    public PostBook() {
    }

    public PostBook(String title, String author, String genre, String isbn, int pages) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pages = pages;
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
}
