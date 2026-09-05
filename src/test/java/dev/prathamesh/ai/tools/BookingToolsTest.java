package dev.prathamesh.ai.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookingToolsTest {

    @Autowired
    private BookingTools bookingTools;

    @Test
    void getBookingStatus_returnsCorrectDetails() {
        // use a bookingId you know exists from your earlier curl/demo data
    	Long id=(long) 3;
        BookingTools.BookingStatusResult result = bookingTools.getBookingStatus(id);

        assertEquals(1, result.bookingId());
        assertEquals("CONFIRMED", result.status());
    }
}