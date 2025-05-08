package one.microstream.domain.microstream;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Book
{
    private String isbn;
    private Integer postId;
    private String title;
    private String author;
    private String genre;
    private int pages;

    public Book() {
    }

    public Book(final String isbn, final Integer postId, final String title, final String author, final String genre, final int pages) {
        this.isbn = isbn;
        this.postId = postId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pages = pages;
    }

    public Integer getPostId() {
        return this.postId;
    }

    public void setPostId(final Integer postId) {
        this.postId = postId;
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
