package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByItemIdTest() {
        User author = userRepository.save(User.builder().name("John Doe").email("john@example.com").build());
        Item item = itemRepository.save(Item.builder().name("Laptop").description("Powerful laptop for rent")
                .available(true).owner(author).build());

        Comment comment1 = createComment("Great item!", item, author);
        Comment comment2 = createComment("Excellent service!", item, author);

        List<Comment> comments = commentRepository.findByItemId(item.getId());

        assertThat(comments).containsExactly(comment1, comment2);
    }

    private Comment createComment(String text, Item item, User author) {
        Comment comment = Comment.builder().text(text).item(item).author(author).build();
        comment.setCreated(LocalDateTime.now());
        return commentRepository.save(comment);
    }
}
