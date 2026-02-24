package one.microstream.domain.indices;

import io.micronaut.eclipsestore.RootProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.microstream.Company;
import org.eclipse.store.gigamap.types.IndexerString;

@Singleton
public class BookIndices
{
	@Inject
	RootProvider<Company>	rootProvider;

	public final static IndexerString<one.microstream.domain.microstream.Book> ISBNIndex = new IndexerString.Abstract<Book>()
	{
		public String name()
		{
			return "isbn";
		}

		@Override
		public String getString(final Book entity)
		{
			return entity.getIsbn();
		}
	};

    public final static IndexerString<one.microstream.domain.microstream.Book> TitleIndex = new IndexerString.Abstract<Book>()
    {
        public String name()
        {
            return "title";
        }

        @Override
        public String getString(final Book entity)
        {
            return entity.getTitle();
        }
    };
}
