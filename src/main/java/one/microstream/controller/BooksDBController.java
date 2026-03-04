package one.microstream.controller;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import one.microstream.core.faker.DataFakerService;
import one.microstream.dao.microstream.postgres.PostDAOBook;
import one.microstream.domain.postgres.PostBook;
import one.microstream.dto.DtoBook;

import java.util.List;
import java.util.stream.Collectors;

@Controller("/db/books")
public class BooksDBController
{
    @Inject
    PostDAOBook postDAOBook;
    @Inject
    DataFakerService dataFakerService;

    @Post()
    @ExecuteOn(TaskExecutors.BLOCKING)
    HttpResponse<?> insert(@Body DtoBook dto)
    {
        PostBook inserted = postDAOBook.insert(dto);
        return HttpResponse.ok(inserted);
    }

    @Post("/createMockupBooks/{amount}")
    HttpResponse<List<PostBook>> createMockupBooks(@NonNull @NotBlank @PathVariable Integer amount)
    {
        List<PostBook> books = dataFakerService.createBooks(amount);
        List<PostBook> postBooks = books.stream().map(book -> postDAOBook.insert(book))
                .collect(Collectors.toUnmodifiableList());
        return HttpResponse.ok(postBooks);
    }
}