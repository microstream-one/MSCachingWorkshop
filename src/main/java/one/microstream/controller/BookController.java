package one.microstream.controller;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import one.microstream.core.mapper.MapperBook;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.dao.microstream.postgres.PostDAOBook;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.postgres.PostBook;
import one.microstream.dto.DtoBook;
import one.microstream.repositories.RepoBook;

@Controller("/books")
public class BookController
{
    @Inject
    DAOBook daoBook;
    @Inject
    PostDAOBook postDAOBook;
    @Inject
    MapperBook mapperBook;

    @Get()
    HttpResponse<?> findAll()
    {
        return HttpResponse.ok(daoBook.findAll());
    }

    @Post
    HttpResponse<?> insert(@Body DtoBook dto)
    {
        // PostgreSQL Save
        PostBook inserted = postDAOBook.insert(dto);

        // Mirostream store
        daoBook.insert(mapperBook.toNewMSBook(dto));

        return HttpResponse.ok(inserted);
    }

    @Post("/postgresOnly")
    @ExecuteOn(TaskExecutors.BLOCKING)
    HttpResponse<?> insertPostgresOnly(@Body DtoBook dto)
    {
        PostBook inserted = postDAOBook.insert(dto);
        return HttpResponse.ok(inserted);
    }
}
