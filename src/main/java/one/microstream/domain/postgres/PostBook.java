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
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private int pages;
    @AutoPopulated
    @Column(name = "created",  insertable = false, updatable = false)
    private LocalDateTime created;
    @AutoPopulated
    @Column(name = "updated",  insertable = false, updatable = false)
    private LocalDateTime updated;

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
