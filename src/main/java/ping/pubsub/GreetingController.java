package ping.pubsub;

import com.coxautodev.graphql.tools.SchemaParser;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.schema.GraphQLSchema;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import ping.pubsub.model.Author;
import ping.pubsub.model.Book;
import ping.pubsub.model.ResponseInfo;
import ping.pubsub.model.SubscriberInfo;
import ping.pubsub.repository.AuthorRepository;
import ping.pubsub.repository.BookRepository;
import ping.pubsub.resolver.Query;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import graphql.GraphQL;

@Controller
public class GreetingController {

    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private Subject<Book, Book> subject = PublishSubject.create();
    private SimpMessagingTemplate messagingTemplate;

    public GreetingController(SimpMessagingTemplate messagingTemplate,
                              BookRepository bookRepository,
                              AuthorRepository authorRepository){
        this.messagingTemplate = messagingTemplate;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @MessageMapping("/hello")
    public void greeting(SubscriberInfo info) throws Exception {


        subject.subscribe(new Subscriber<Book>() {
            @Override
            public void onCompleted() {}
            @Override
            public void onError(Throwable throwable) {}

            @Override
            public void onNext(Book book) {
                GraphQLSchema schema = SchemaParser.newParser()
                        .file("book.graphqls")
                        .resolvers(new Query(book))
                        .build().makeExecutableSchema();
                GraphQL graphQL = GraphQL.newGraphQL(schema).build();
                ExecutionInput executionInput = ExecutionInput.newExecutionInput().query(info.getSchema()).build();
                ExecutionResult executionResult = graphQL.execute(executionInput);
                messagingTemplate.convertAndSend("/topic/greetings/" + info.getName(),
                                                    new ResponseInfo(executionResult.getData().toString()));
            }
        });

    }

    @PostMapping("/addAuthor")
    @ResponseStatus(value = HttpStatus.OK)
    public void addAuthor(@RequestParam("id") Long id,
                          @RequestParam("firstName") String firstName,
                          @RequestParam("lastName") String lastName){
        Author author = new Author(id, firstName, lastName);
        authorRepository.save(author);
    }

    @PostMapping("/addBook")
    @ResponseStatus(value = HttpStatus.OK)
    public void addAuthor(@RequestParam("title") String title,
                            @RequestParam("isbn") String isbn,
                            @RequestParam("pageCount") int pageCount,
                            @RequestParam("author") Long authorId){
        Book book = new Book(title, isbn, pageCount, authorRepository.findById(authorId));
        bookRepository.save(book);
        subject.onNext(book);
    }

}