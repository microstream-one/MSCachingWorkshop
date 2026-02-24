package one.microstream.domain.microstream;

import one.microstream.domain.indices.BookIndices;
import org.eclipse.store.gigamap.types.BitmapIndices;
import org.eclipse.store.gigamap.types.GigaMap;

import java.util.ArrayList;
import java.util.List;

public class Company
{
    public GigaMap<Book> gigaBooks	= GigaMap.New();

    public Company()
    {
        super();

        final BitmapIndices<Book> indices = gigaBooks.index().bitmap();
        indices.add(BookIndices.ISBNIndex);
        indices.add(BookIndices.TitleIndex);
    }

    public GigaMap<Book> getGigaBooks() {
        return gigaBooks;
    }

    public void setGigaBooks(GigaMap<Book> gigaBooks) {
        this.gigaBooks = gigaBooks;
    }
}
