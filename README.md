## Micronaut 4.8.2 Documentation

- [User Guide](https://docs.micronaut.io/4.8.2/guide/index.html)
- [API Reference](https://docs.micronaut.io/4.8.2/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/4.8.2/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---
```sql
create function notify_data_change() returns trigger
    language plpgsql
as
$$
DECLARE
payload JSON;
BEGIN
  payload = json_build_object(
    'table', TG_TABLE_NAME,
    'operation', TG_OP,
    'data', row_to_json(NEW)
  );
  PERFORM pg_notify('data_changed', payload::text);
RETURN NEW;
END;
$$;

alter function notify_data_change() owner to workshopuser;
```
## Trigger that needs to be added to the table

```sql
-- auto-generated definition
create trigger data_change_trigger
    after insert or update
    on books
    for each row
execute procedure notify_data_change();
```
- [Micronaut Maven Plugin documentation](https://micronaut-projects.github.io/micronaut-maven-plugin/latest/)
## Feature eclipsestore documentation

- [Micronaut EclipseStore documentation](https://micronaut-projects.github.io/micronaut-eclipsestore/latest/guide)

- [https://docs.eclipsestore.io/](https://docs.eclipsestore.io/)


## Feature micronaut-aot documentation

- [Micronaut AOT documentation](https://micronaut-projects.github.io/micronaut-aot/latest/guide/)


## Feature serialization-jackson documentation

- [Micronaut Serialization Jackson Core documentation](https://micronaut-projects.github.io/micronaut-serialization/latest/guide/)


## Feature maven-enforcer-plugin documentation

- [https://maven.apache.org/enforcer/maven-enforcer-plugin/](https://maven.apache.org/enforcer/maven-enforcer-plugin/)


