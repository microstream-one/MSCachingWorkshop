package one.microstream.controller;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import one.microstream.core.faker.DataFakerService;
import one.microstream.core.mapper.MapperBook;
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
    @Inject
    MapperBook mapperBook;

    @Post()
    @ExecuteOn(TaskExecutors.BLOCKING)
    HttpResponse<?> insert(@Body DtoBook dto)
    {
        PostBook inserted = postDAOBook.insert(dto);
        return HttpResponse.ok(inserted);
    }

    @Put("/update")
    HttpResponse<?> updateBook(@Body DtoBook dto)
    {
        try {
            PostBook updated = postDAOBook.update(dto);
            return HttpResponse.ok(mapperBook.toDto(updated));
        } catch (Exception ex) {
            return HttpResponse.notFound("Book to update not found");
        }
    }

    @Delete("/delete/{id}")
    HttpResponse<?> deleteBook(@NonNull @NotBlank @PathVariable Integer id)
    {
        try {
            postDAOBook.delete(id);
            return HttpResponse.ok("Book successfully deleted");
        } catch (Exception ex) {
            return HttpResponse.notFound("Book to update not found");
        }
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