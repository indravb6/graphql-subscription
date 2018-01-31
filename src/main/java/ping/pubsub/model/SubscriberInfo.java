package ping.pubsub.model;

public class SubscriberInfo {

    private String name;
    private String schema;

    public SubscriberInfo() {
    }

    public SubscriberInfo(String name, String schema) {
        this.name = name;
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}