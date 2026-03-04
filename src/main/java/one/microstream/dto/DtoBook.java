package one.microstream.dto;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record DtoBook(
        Integer id,
        String title,
        String author,
        String genre,
        String isbn,
        int pages,
        String publisher,
        int year,
        String description,
        String language,
        double price) {
}
