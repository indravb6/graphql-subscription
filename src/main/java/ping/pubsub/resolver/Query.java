package ping.pubsub.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import ping.pubsub.model.Book;

public class Query implements GraphQLQueryResolver {

    private Book book;

    public Query(Book book) {
        this.book = book;
    }

    public Book bookAdded() {
        return book;
    }
}