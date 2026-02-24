package one.microstream.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import one.microstream.dao.microstream.postgres.PostDAOBook;
import one.microstream.domain.postgres.PostBook;
import one.microstream.dto.DtoBook;

@Controller("/db/books")
public class BooksDBController
{
    @Inject
    PostDAOBook postDAOBook;

    @Post()
    @ExecuteOn(TaskExecutors.BLOCKING)
    HttpResponse<?> insert(@Body DtoBook dto)
    {
        PostBook inserted = postDAOBook.insert(dto);
        return HttpResponse.ok(inserted);
    }
}
