package com.tgrajkowski.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PeselDateExtractor {
    public LocalDate getDateOfBirthFromPesel(String pesel) {
        List<Integer> peselNumberList = Arrays.stream(pesel.split(""))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int year = 1900 + peselNumberList.get(0) * 10 + peselNumberList.get(1);
        if (peselNumberList.get(2) >= 2 && peselNumberList.get(2) < 8) {
            year += Math.floor(peselNumberList.get(2) / 2) * 100;
        }
        if (peselNumberList.get(2) >= 8) {
            year -= 100;
        }
        int month = peselNumberList.get(2) % 2 * 10 + peselNumberList.get(3);
        int day = peselNumberList.get(4) * 10 + peselNumberList.get(5);

        return LocalDate.of(year, month, day);
    }
}
