package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import javax.validation.Valid;
import java.util.List;

/*
    Контроллер для управления запросами предметов
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemServiceImpl service;

    /*
        Создать новый предмет
     */
    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid Item item) {
        log.info("Выполняется POST-запрос. Создание нового предмета.");
        return service.createItem(userId, item);
    }

    /*
        Обновить информацию предмета
     */
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId, @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("Выполняется PATCH-запрос. Обновление существующего предмета.");
        return service.updateItem(itemId, item, idUser);
    }

    /*
        Получить предмет по id
     */
    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long idUser) {
        log.info("Выполняется GET-запрос. Получение предмета по id.");
        return service.getItemById(itemId, idUser);
    }

    /*
        Получить все предметы пользователя
    */
    @GetMapping
    public List<ItemDto> getItemByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Выполняется GET-запрос. Получение предметов пользователя.");
        return service.getItemByUser(userId);
    }

    /*
        Найти предмет по названию или описанию
     */
    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String textQuery) {
        log.info("Выполняется GET-запрос. Получение предмета по отрезку фразы.");
        return service.searchItem(textQuery);
    }

    /*
        Создать новый отзыв
     */
    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId,
                                    @RequestBody @Valid Comment comment) {
        log.info("Выполняется POST-запрос. Добавить комментарий.");
        return service.createComment(itemId, userId, comment);
    }
}