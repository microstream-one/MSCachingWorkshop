package one.microstream.core.init;

import one.microstream.domain.postgres.PostBook;

public class DatabaseEvent
{
    private String table;
    private String operation;
    private PostBook data;

    // Constructors, getters, and setters
    public DatabaseEvent() {}

    public DatabaseEvent(String table, String operation, PostBook data) {
        this.table = table;
        this.operation = operation;
        this.data = data;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public PostBook getData() {
        return data;
    }

    public void setData(PostBook data) {
        this.data = data;
    }
}
