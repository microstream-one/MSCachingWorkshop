package one.microstream.controller;

import java.util.List;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import one.microstream.core.mapper.MapperBook;
import one.microstream.core.ollama.EmbeddingService;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.domain.microstream.Book;
import one.microstream.dto.DtoBook;

@Controller("/cache/books")
public class BookCacheController
{
    @Inject
    DAOBook daoBook;
    @Inject
    MapperBook mapperBook;

    private final EmbeddingService embeddingService = new EmbeddingService();

    @Get("/page/{limit}")
    List<DtoBook> pageAllBooks(@NonNull @PathVariable final int limit)
    {
        return this.daoBook.pageBooks(limit).stream().map(this.mapperBook::toDto).toList();
    }

    @Get("/search")
    public List<Book> searchBooks(
        @NonNull @QueryValue final Float score,
        @NonNull @NotBlank @QueryValue final String titleSearch
    )
    {
        final float[] vectorized = this.embeddingService.vectorize(titleSearch);
        return this.daoBook.searchBooks(vectorized, score);
    }

    @Get("/vectorize/{text}")
    float[] pageAllBooks(@NonNull @NotBlank @PathVariable final String text)
    {
        return this.embeddingService.vectorize(text);
    }
}
