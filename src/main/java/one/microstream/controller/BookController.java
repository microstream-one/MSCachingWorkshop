package one.microstream.controller;


import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.domain.microstream.Book;
import one.microstream.repositories.RepoBook;

@Controller("/books")
public class BookController
{
    @Inject
    DAOBook daoBook;
    @Inject
    private RepoBook repoBook;

    @Get()
    HttpResponse<?> findAll()
    {
        return HttpResponse.ok(daoBook.findAll());
    }

    @Post
    HttpResponse<?> insert(@Body Book book)
    {
        // PostgreSQL Save
        Book saved = repoBook.save(book);

        // Mirostream store
        daoBook.insert(book);

        return HttpResponse.ok(saved);
    }
}
