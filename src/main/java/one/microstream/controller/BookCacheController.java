package one.microstream.controller;


import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import one.microstream.core.mapper.MapperBook;
import one.microstream.core.ollama.EmbeddingService;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.domain.microstream.Book;
import one.microstream.dto.DtoBook;

import java.util.List;
import java.util.stream.Collectors;

@Controller("/cache/books")
public class BookCacheController
{
    @Inject
    DAOBook daoBook;
    @Inject
    MapperBook mapperBook;
    @Inject
    EmbeddingService embeddingService;

    @Get("/page/{limit}")
    List<DtoBook> pageAllBooks(@NonNull @NotBlank @PathVariable int limit)
    {
        return daoBook.pageBooks(limit).stream().map(b -> mapperBook.toDto(b)).collect(Collectors.toUnmodifiableList());
    }

    @Get("/createVector/{text}")
    float[] pageAllBooks(@NonNull @NotBlank @PathVariable String text)
    {
        return embeddingService.vectorize(text);
    }

    @Post("/vectorizeCache")
    HttpResponse<?> vectorizeAllBooks()
    {
        List<Book> books = daoBook.allBooks();
        embeddingService.embeddAllBooks(books, 100);
        return HttpResponse.ok();
    }
}
