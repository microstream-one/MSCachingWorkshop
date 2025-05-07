package one.microstream.domain.microstream;

import java.util.ArrayList;
import java.util.List;

public class Company
{
    private List<Book> books = new ArrayList<Book>();

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
