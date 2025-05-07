package one.microstream.domain.postgres;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.persistence.*;

@Serdeable
@Entity
@Cacheable(true)
@Table(name = "books", schema = "public")
public class PostCustomer
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer			id;

    @Column(nullable = false)
    private String			name;

    @Column(length = 500)
    private String			description;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
