package one.microstream.controller;

import java.util.ArrayList;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import one.microstream.core.faker.DataFakerService;
import one.microstream.core.mapper.MapperBook;
import one.microstream.dao.postgres.PostDAOBook;
import one.microstream.domain.postgres.PostBook;
import one.microstream.dto.DtoBook;

@Controller("/db/books")
public class BooksDBController
{
    @Inject
    PostDAOBook dao;
    @Inject
    DataFakerService dataFakerService;
    @Inject
    MapperBook mapper;

    @Post
    @ExecuteOn(TaskExecutors.BLOCKING)
    public PostBook insert(@NonNull @Body final DtoBook dto)
    {
        return this.dao.insert(dto);
    }

    @Put("/update")
    public HttpResponse<?> updateBook(@NonNull @Body final DtoBook dto)
    {
        try
        {
            final var book = this.dao.update(dto);
            return HttpResponse.ok(this.mapper.toDto(book));
        }
        catch (final Exception ignored)
        {
            return HttpResponse.notFound("Book to update not found");
        }
    }

    @Delete("/delete/{id}")
    HttpResponse<?> deleteBook(@NonNull @NotBlank @PathVariable Integer id)
    {
        try
        {
            this.dao.delete(id);
            return HttpResponse.ok("Book successfully deleted");
        }
        catch (final Exception ignored)
        {
            return HttpResponse.notFound("Book to update not found");
        }
    }

    @Post("/createMockupBooks/{amount}")
    long createMockupBooks(@NonNull @Positive @PathVariable final Integer amount)
    {
        final var split = this.dataFakerService.createBooks(amount).spliterator();
        final int chunkSize = 100;
        long total = 0;
        while (true)
        {
            final var chunk = new ArrayList<PostBook>(chunkSize);
            for (int i = 0; i < chunkSize && split.tryAdvance(chunk::add); i++)
            {
                total++;
            }
            if(chunk.isEmpty()) break;
            System.out.println("Inserting books at " + total);
            this.dao.insertAll(chunk);
        }
        return total;
    }
}
