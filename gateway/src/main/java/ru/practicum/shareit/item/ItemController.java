package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.item.ItemDtoIn;
import ru.practicum.shareit.item.model.Comment;

import javax.validation.Valid;

/*
    Контроллер для управления запросами предметов
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    @Autowired
    private ItemClient itemClient;

    /*
        Создать новый предмет
     */
    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDtoIn item) {
        log.info("Выполняется POST-запрос. Создание нового предмета.");
        return itemClient.createItem(userId, item);
    }

    /*
        Обновить информацию предмета
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId, @RequestBody ItemDtoIn item,
                                             @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("Выполняется PATCH-запрос. Обновление существующего предмета.");
        return itemClient.updateItem(idUser, item, itemId);
    }

    /*
        Получить предмет по id
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("Выполняется GET-запрос. Получение предмета по id.");
        return itemClient.getItemById(itemId, idUser);
    }

    /*
        Получить все предметы пользователя
    */
    @GetMapping
    public ResponseEntity<Object> getItemByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Выполняется GET-запрос. Получение предметов пользователя.");
        return itemClient.getItemByUser(userId);
    }

    /*
        Найти предмет по названию или описанию
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestParam("text") String textQuery) {
        log.info("Выполняется GET-запрос. Получение предмета по отрезку фразы.");
        return itemClient.searchItem(textQuery);
    }

    /*
        Создать новый отзыв
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody @Valid Comment comment) {
        log.info("Выполняется POST-запрос. Добавить комментарий.");
        return itemClient.createComment(itemId, userId, comment);
    }
}