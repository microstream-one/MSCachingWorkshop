package one.microstream.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import one.microstream.dao.microstream.DAOBook;
import one.microstream.domain.microstream.Book;
import one.microstream.domain.postgres.PostBook;
import one.microstream.repositories.RepoBook;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.serializer.reference.Lazy;
import org.eclipse.serializer.reference.LazyReferenceManager;

@Controller("/perf/books")
public class PerformanceController
{
    @Inject
    DAOBook es;
    @Inject
    RepoBook pg;

    private final List<String> isbns = new ArrayList<>();
    private final Random random = new Random();

    @Get("/title/{titleSearch}")
    PerfResponse titleSearch(@NotBlank @QueryValue final String titleSearch)
    {
        final String pgTitleSearch = "%" + titleSearch + "%";

        final List<Book> esBooks;
        final List<PostBook> pgBooks;
        final long esTime, pgTime;

        {
            final var res = this.time(() -> this.es.findByTitle(titleSearch));
            esBooks = res.getLeft();
            esTime = res.getRight();
        }

        {
            final var res = this.time(() -> this.pg.findByTitleIlike(pgTitleSearch));
            pgBooks = res.getLeft();
            pgTime = res.getRight();
        }

        System.out.format("ES found %d books in %d ns with a title matching %s%n", esBooks.size(), esTime, titleSearch);
        System.out.format("PG found %d books in %d ns with a title matching %s%n", pgBooks.size(), pgTime, titleSearch);

        if (pgBooks.size() != esBooks.size())
        {
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ES and PG book count differs");
        }

        return new PerfResponse(esTime, pgTime);
    }

    @Get("/random/isbn/refresh")
    void refreshRandomIsbn()
    {
        // update the cached isbns for random access
        this.isbns.clear();
        this.isbns.addAll(this.es.findAllIsbn());
        System.out.printf("Caching %d book isbns", this.isbns.size());
    }

    @Get("/clean")
    void clean()
    {
        LazyReferenceManager.get().cleanUp(Lazy.Checker(0.25));
        System.gc();
    }

    @Get("/random/isbn")
    PerfResponse randomIsbn()
    {
        final String isbn = this.isbns.get(this.random.nextInt(this.isbns.size()));

        final boolean esFound, pgFound;
        final long esTime, pgTime;

        {
            final var res = this.time(() -> this.es.findByIsbn(isbn));
            esFound = res.getLeft().isPresent();
            esTime = res.getRight();
        }

        {
            final var res = this.time(() -> this.pg.findByIsbn(isbn));
            pgFound = res.getLeft().isPresent();
            pgTime = res.getRight();
        }

        System.out.format("ES found book in %d ns with isbn %s%n", esTime, isbn);
        System.out.format("PG found book in %d ns with isbn %s%n", pgTime, isbn);

        if (esFound != pgFound)
        {
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "ES and PG isbn book differs");
        }

        return new PerfResponse(esTime, pgTime);
    }

    private <T> Pair<T, Long> time(final Supplier<T> task)
    {
        final long startNs = System.nanoTime();

        final T result = task.get();

        final long timeNs = System.nanoTime() - startNs;

        return Pair.of(result, timeNs);
    }

    @Serdeable
    public record PerfResponse(long eclipseStoreNs, long postgresNs)
    {
    }
}
