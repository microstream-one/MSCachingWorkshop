package one.microstream.domain.microstream;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Book
{
    private Integer postId;
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private int pages;
    private String publisher;
    private int year;
    private String description;
    private String language;
    private double price;

    public Book()
    {
    }

    public Book(
        final Integer postId,
        final String isbn,
        final String title,
        final String author,
        final String genre,
        final int pages,
        final String publisher,
        final int year,
        final String description,
        final String language,
        final double price
    )
    {
        this.postId = postId;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.pages = pages;
        this.publisher = publisher;
        this.year = year;
        this.description = description;
        this.language = language;
        this.price = price;
    }

    public Integer getPostId()
    {
        return this.postId;
    }

    public void setPostId(final Integer postId)
    {
        this.postId = postId;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
    }

    public String getAuthor()
    {
        return this.author;
    }

    public void setAuthor(final String author)
    {
        this.author = author;
    }

    public String getGenre()
    {
        return this.genre;
    }

    public void setGenre(final String genre)
    {
        this.genre = genre;
    }

    public String getIsbn()
    {
        return this.isbn;
    }

    public void setIsbn(final String isbn)
    {
        this.isbn = isbn;
    }

    public int getPages()
    {
        return this.pages;
    }

    public void setPages(final int pages)
    {
        this.pages = pages;
    }

    public String getPublisher()
    {
        return this.publisher;
    }

    public void setPublisher(final String publisher)
    {
        this.publisher = publisher;
    }

    public int getYear()
    {
        return this.year;
    }

    public void setYear(final int year)
    {
        this.year = year;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public String getLanguage()
    {
        return this.language;
    }

    public void setLanguage(final String language)
    {
        this.language = language;
    }

    public double getPrice()
    {
        return this.price;
    }

    public void setPrice(final double price)
    {
        this.price = price;
    }
}
