package ping.pubsub.repository;

import org.springframework.data.repository.CrudRepository;
import ping.pubsub.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    Book findById(long id);
}