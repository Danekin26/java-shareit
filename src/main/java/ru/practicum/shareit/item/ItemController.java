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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    /*
        Создать новый предмет
     */
    @PostMapping
    public Item createItem(@RequestHeader("X-Sharer-User-Id") int userId, @RequestBody @Valid Item item) {
        log.info("Выполняется POST-запрос. Создание нового предмета.");
        return service.createItem(userId, item);
    }

    /*
        Обновить информацию предмета
     */
    @PatchMapping("/{itemId}")
    public Item updateItem(@PathVariable int itemId, @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") int idUser) {
        log.info("Выполняется PATCH-запрос. Обновление существующего предмета.");
        return service.updateItem(itemId, item, idUser);
    }

    /*
        Получить предмет по id
     */
    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable int itemId) {
        log.info("Выполняется GET-запрос. Получение предмета по id.");
        return service.getItemById(itemId);
    }

    /*
        Получить все предметы пользователя
    */
    @GetMapping
    public List<Item> getItemByUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Выполняется GET-запрос. Получение предметов пользователя.");
        return service.getItemByUser(userId);
    }

    /*
        Найти предмет по названию или описанию
     */
    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam("text") String textQuery) {
        log.info("Выполняется GET-запрос. Получение предмета по отрезку фразы.");
        return service.searchItem(textQuery);
    }
}