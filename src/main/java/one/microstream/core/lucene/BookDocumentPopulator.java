package one.microstream.core.lucene;

import one.microstream.domain.microstream.Book;
import org.apache.lucene.document.Document;
import org.eclipse.store.gigamap.lucene.DocumentPopulator;

public class BookDocumentPopulator extends DocumentPopulator<Book>
{
    public static final String TITLE_FIELD = "title";

    @Override
    public void populate(final Document document, final Book entity)
    {
        document.add(createTextField(TITLE_FIELD, entity.getTitle()));
    }
}
