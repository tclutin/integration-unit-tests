package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDates {
    private String checkin;
    private String checkout;

    public BookingDates(String format, String format1, Object o) {
        this.checkin = format;
        this.checkout = format1;
    }
}
