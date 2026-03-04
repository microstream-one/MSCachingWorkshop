package one.microstream.core.mapper;

import jakarta.inject.Singleton;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.postgres.PostBook;
import one.microstream.dto.DtoBook;

@Singleton
public class MapperBook
{
    public DtoBook toDto(final Book book) {
        return new DtoBook(book.getPostId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getIsbn(),
                book.getPages(), book.getPublisher(), book.getYear(), book.getDescription(), book.getLanguage(),
                book.getPrice());
    }

    public DtoBook toDto(final PostBook book) {
        return new DtoBook(book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getIsbn(),
                book.getPages(), book.getPublisher(), book.getYear(), book.getDescription(), book.getLanguage(),
                book.getPrice());
    }

    public PostBook toNewPostBook(final DtoBook dto) {
        return new PostBook(dto.title(), dto.author(), dto.genre(), dto.isbn(), dto.pages(), dto.publisher(),
                dto.year(), dto.description(), dto.language(), dto.price());
    }

    public String toEmbeddingText(final PostBook b) {
        return String.format("Titel: %s; Autor: %s; Inhalt: %s",
                b.getTitle(), b.getAuthor(), b.getDescription());
    }

    public String toEmbeddingText(final Book b) {
        return String.format("Titel: %s; Autor: %s; Inhalt: %s",
                b.getTitle(), b.getAuthor(), b.getDescription());
    }
}