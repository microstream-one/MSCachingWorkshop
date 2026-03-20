package one.microstream.controller;

import java.util.List;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import one.microstream.core.mapper.MapperBook;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.dto.DtoBook;

@Controller("/cache/books")
public class BookCacheController
{
    @Inject
    DAOBook daoBook;
    @Inject
    MapperBook mapperBook;

    @Get("/page/{limit}")
    List<DtoBook> pageAllBooks(
        @NonNull @NotBlank @PathVariable final int limit,
        @Nullable @QueryValue final Integer offset
    )
    {
        final var books = offset == null ? this.daoBook.pageBooks(limit) : this.daoBook.pageBooks(limit, offset);
        return books.stream().map(this.mapperBook::toDto).toList();
    }
}
