package one.microstream.controller;


import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import one.microstream.core.mapper.MapperBook;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.dao.microstream.postgres.PostDAOBook;
import one.microstream.domain.postgres.PostBook;
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

    @Get("/page/{limit}")
    List<DtoBook> pageAllBooks(@NonNull @NotBlank @PathVariable int limit)
    {
        return daoBook.pageBooks(limit).stream().map(b -> mapperBook.toDto(b)).collect(Collectors.toUnmodifiableList());
    }


}
