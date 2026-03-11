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

    public PostBook updatePostBook(final PostBook toUpdate, final DtoBook dto) {
        toUpdate.setAuthor(dto.author());
        toUpdate.setTitle(dto.title());
        toUpdate.setDescription(dto.description());
        toUpdate.setPrice(dto.price());
        toUpdate.setPublisher(dto.publisher());
        toUpdate.setIsbn(dto.isbn());
        toUpdate.setDescription(dto.description());
        toUpdate.setGenre(dto.genre());
        toUpdate.setLanguage(dto.language());
        toUpdate.setPages(dto.pages());
        toUpdate.setYear(dto.year());

        return toUpdate;
    }
}