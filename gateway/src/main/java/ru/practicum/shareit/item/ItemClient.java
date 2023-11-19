package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.item.ItemDtoIn;
import ru.practicum.shareit.item.model.Comment;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long userId, ItemDtoIn item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> updateItem(long userId, ItemDtoIn item, long itemId) {
        return patch("/" + itemId, userId, item);
    }

    public ResponseEntity<Object> getItemById(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getItemByUser(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> searchItem(String textQuery) {
        return get("/search?text=" + textQuery);
    }

    public ResponseEntity<Object> createComment(long itemId, long userId, Comment comment) {
        return post("/" + itemId + "/comment", userId, comment);
    }
}
