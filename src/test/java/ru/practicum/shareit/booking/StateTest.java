package ru.practicum.shareit.booking;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.StateSearch;
import ru.practicum.shareit.exception.InvalidDataEnteredException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class StateTest {
    @Test
    void getEnumValueTest() {

        String stateStr;
        stateStr = "Unknown";
        String finalStateStr = stateStr;
        assertThrows(InvalidDataEnteredException.class, () -> StateSearch.getEnumValue(finalStateStr));

        StateSearch stateTest;

        stateStr = "CURRENT";
        stateTest = StateSearch.getEnumValue(stateStr);
        assertEquals(stateTest, StateSearch.CURRENT);

        stateStr = "PAST";
        stateTest = StateSearch.getEnumValue(stateStr);
        assertEquals(stateTest, StateSearch.PAST);

        stateStr = "FUTURE";
        stateTest = StateSearch.getEnumValue(stateStr);
        assertEquals(stateTest, StateSearch.FUTURE);

        stateStr = "REJECTED";
        stateTest = StateSearch.getEnumValue(stateStr);
        assertEquals(stateTest, StateSearch.REJECTED);

        stateStr = "WAITING";
        stateTest = StateSearch.getEnumValue(stateStr);
        assertEquals(stateTest, StateSearch.WAITING);
    }
}
