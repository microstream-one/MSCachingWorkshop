package one.microstream.core.mapper;

import jakarta.inject.Singleton;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.postgres.PostBook;
import one.microstream.dto.DtoBook;

@Singleton
public class MapperBook
{
    public DtoBook toDto(final Book book) {
        return new DtoBook(book.getPostId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getIsbn(), book.getPages());
    }

    public DtoBook toDto(final PostBook book) {
        return new DtoBook(book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getIsbn(), book.getPages());
    }

    public Book toNewMSBook(final DtoBook dto) {
        return new Book(dto.isbn(), dto.id(), dto.title(), dto.author(), dto.genre(), dto.pages());
    }

    public PostBook toNewPostBook(final DtoBook dto) {
        return new PostBook(dto.title(), dto.author(), dto.genre(), dto.isbn(), dto.pages());
    }
}