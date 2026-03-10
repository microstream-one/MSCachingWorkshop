package one.microstream.controller;


import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import one.microstream.core.mapper.MapperBook;
import one.microstream.core.ollama.EmbeddingService;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.domain.indices.BookIndices;
import one.microstream.domain.microstream.Book;
import one.microstream.dto.DtoBook;
import org.eclipse.store.gigamap.jvector.VectorIndex;

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
        return daoBook.pageBooks(limit).stream().map(b -> mapperBook.toDto(b))
                .collect(Collectors.toUnmodifiableList());
    }

    @Get("/search{?filter*}")
    public HttpResponse<?> searchBooks(@RequestBean RBBookFilter filter)
    {
//        String embeddingText = embeddingService.toEmbeddingText(filter);
        float[] vectorize = embeddingService.vectorize("search_query: " + filter.title());
        List<Book> books = daoBook.searchBooks(vectorize);

        return HttpResponse.ok(books);
    }

    @Get("/createVector/{text}")
    float[] pageAllBooks(@NonNull @NotBlank @PathVariable String text)
    {
        return embeddingService.vectorize(text);
    }
}
