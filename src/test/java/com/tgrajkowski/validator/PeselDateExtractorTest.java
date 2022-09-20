package com.tgrajkowski.validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

class PeselDateExtractorTest {

    @ParameterizedTest
    @MethodSource("peselListWithDates")
    void getDateOfBirthFromPesel(String pesel, LocalDate expectedDate) {
        PeselDateExtractor peselDateExtractor = new PeselDateExtractor();
        LocalDate dateOfBirth = peselDateExtractor.getDateOfBirthFromPesel(pesel);
        Assertions.assertThat(dateOfBirth).isEqualTo(expectedDate);
    }

    private static Stream<Arguments> peselListWithDates() {
        return Stream.of(
                Arguments.of("81012189633", LocalDate.of(1981, 1, 21)),
                Arguments.of("91020124175", LocalDate.of(1991, 2, 1)),
                Arguments.of("00251062758", LocalDate.of(2000, 5, 10)),
                Arguments.of("10320156756", LocalDate.of(2010, 12, 1))
        );
    }
}