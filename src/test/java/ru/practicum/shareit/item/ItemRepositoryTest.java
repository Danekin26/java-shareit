package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findAllByOwner_IdOrderByIdAscTest() {
        User user = createUser("John Doe", "john@example.com");
        Item item1 = createItem("Laptop", "Powerful laptop for rent", true, user);
        Item item2 = createItem("Camera", "High-quality camera for photography", true, user);

        List<Item> userItems = itemRepository.findAllByOwner_IdOrderByIdAsc(user.getId());

        assertThat(userItems).containsExactly(item1, item2);
    }

    @Test
    void findAllByAvailableAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCaseTest() {
        User user = createUser("John Doe", "john@example.com");
        Item availableItem1 = createItem("Laptop", "Powerful laptop for rent", true, user);
        Item availableItem2 = createItem("Camera", "High-quality camera for photography", true, user);

        List<Item> foundItems = itemRepository.findAllByAvailableAndDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(
                true, "high-quality", "");

        assertThat(foundItems).containsExactly(availableItem1, availableItem2);
    }

    private User createUser(String name, String email) {
        return userRepository.save(User.builder().name(name).email(email).build());
    }

    private Item createItem(String name, String description, boolean available, User owner) {
        return itemRepository.save(Item.builder().name(name).description(description)
                .available(available).owner(owner).build());
    }
}
