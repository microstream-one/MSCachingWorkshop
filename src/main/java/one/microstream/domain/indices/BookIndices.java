package one.microstream.domain.indices;

import one.microstream.domain.microstream.Book;
import org.eclipse.store.gigamap.types.BinaryIndexerInteger;
import org.eclipse.store.gigamap.types.BinaryIndexerString;

public class BookIndices
{
    public final static BinaryIndexerString<Book> ISBN = new BinaryIndexerString.Abstract<>()
    {
        @Override
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

    public final static BinaryIndexerInteger<Book> POST_ID = new BinaryIndexerInteger.Abstract<>()
    {
        @Override
        public String name()
        {
            return "postId";
        }

        @Override
        public Integer getInteger(final Book entity)
        {
            return entity.getPostId();
        }
    };
}
