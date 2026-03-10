package one.microstream.controller;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record RBBookFilter(
        @Nullable String title,
        @Nullable String description,
        @Nullable String author
)
{
}
