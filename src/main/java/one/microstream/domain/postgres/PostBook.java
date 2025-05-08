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

    public PostBook(final String title, final String author, final String genre, final String isbn, final int pages) {
    	this.title = title;
    	this.author = author;
    	this.genre = genre;
    	this.isbn = isbn;
    	this.pages = pages;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    public int getPages() {
        return this.pages;
    }

    public void setPages(final int pages) {
        this.pages = pages;
    }
}
