package one.microstream.domain.microstream;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public class Book
{
    private Integer postId;
    private String title;
    private String author;
    private String genre;
    private String isbn;
    private int pages;
    private String publisher;
    private int year;
    private String description;
    private String language;
    private double price;
    private float[] embeddings;

    public Book() {
    }

    public Book(final Integer postId, final String isbn, final String title, final String author, final String genre,
                final int pages, String publisher, int year, String description, String language,
                double price)
    {
        this.postId = postId;
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

    public float[] getEmbeddings() {
        return embeddings;
    }

    public void setEmbeddings(float[] embeddings) {
        this.embeddings = embeddings;
    }
}
