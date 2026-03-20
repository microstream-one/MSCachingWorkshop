package one.microstream.core.faker;

import jakarta.inject.Singleton;
import net.datafaker.Faker;
import one.microstream.domain.postgres.PostBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Singleton
public class DataFakerService
{
    public List<PostBook> createBooks(Integer amount)
    {
        // Faker mit deutscher Lokalisierung für realistischere Daten
        Faker faker = new Faker(Locale.forLanguageTag("de"));

        List<PostBook> books = new ArrayList<>(amount);

        System.out.println("Generiere " + amount + " Bücher...");

        for (int i = 0; i < amount; i++) {
            books.add(new PostBook(
                    faker.book().title(),            // 2. Titel
                    faker.book().author(),           // 3. Autor
                    faker.book().genre(),            // 4. Genre
                    faker.code().isbn13(),           // 1. ISBN
                    faker.number().numberBetween(100, 1200),   // 10. Seitenzahl
                    faker.book().publisher(),        // 5. Verlag
                    faker.number().numberBetween(1950, 2024), // 6. Erscheinungsjahr
                    faker.matz().quote(),      // 7. Beschreibung (Wichtig für Embeddings!)
                    faker.options().option("DE", "EN", "FR"), // 8. Sprache
                    faker.number().randomDouble(2, 10, 50)   // 9. Preis
            ));

            if (i % 10000 == 0 && i > 0) {
                System.out.println(i + " Bücher erstellt...");
            }
        }

        System.out.println("Fertig! Gesamtzahl: " + books.size());

        return  books;
    }
}
