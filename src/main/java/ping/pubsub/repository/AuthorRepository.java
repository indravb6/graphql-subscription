package ping.pubsub.repository;
import org.springframework.data.repository.CrudRepository;
import ping.pubsub.model.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findById(Long id);
}