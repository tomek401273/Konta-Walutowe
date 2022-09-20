package com.tgrajkowski.validator;

import com.tgrajkowski.data.TestClock;
import com.tgrajkowski.model.dto.AccountDto;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.Clock;
import java.time.LocalDate;

public class AgeValidatorTest {

    private Clock clock;

    @Mock
    private PeselDateExtractor peselDateExtractor;

    private AgeValidator ageValidator;

    @Before
    public void beforeEach() {
        clock = new TestClock("2022-09-23T20:00:00.000000Z");
        MockitoAnnotations.openMocks(this);
        ageValidator = new AgeValidator(peselDateExtractor, clock);
    }


    @Test
    public void shouldPersonBeAdult_18YearsOld() {
        // given
        AccountDto accountDto = AccountDto.builder().pesel("testPesel").build();
        LocalDate localDate = LocalDate.now(clock).minusYears(18);

        Mockito.when(peselDateExtractor.getDateOfBirthFromPesel(Mockito.anyString())).thenReturn(localDate);

        // when
        boolean isPersonAdult = ageValidator.isPersonAdult(accountDto);

        // then
        Assertions.assertThat(isPersonAdult).isTrue();
    }

    @Test
    public void shouldPersonBeNotAdult_Below18YearsOld() {
        // given
        AccountDto accountDto = AccountDto.builder().pesel("testPesel").build();
        LocalDate localDate = LocalDate.now(clock).minusYears(10);

        Mockito.when(peselDateExtractor.getDateOfBirthFromPesel(Mockito.anyString())).thenReturn(localDate);

        // when
        boolean isPersonAdult = ageValidator.isPersonAdult(accountDto);

        // then
        Assertions.assertThat(isPersonAdult).isFalse();
    }


    @Test
    public void shouldPersonBeNotAdult_Above18YearsOld() {
        // given
        AccountDto accountDto = AccountDto.builder().pesel("testPesel").build();
        LocalDate localDate = LocalDate.now(clock).minusYears(20);

        Mockito.when(peselDateExtractor.getDateOfBirthFromPesel(Mockito.anyString())).thenReturn(localDate);

        // when
        boolean isPersonAdult = ageValidator.isPersonAdult(accountDto);

        // then
        Assertions.assertThat(isPersonAdult).isTrue();
    }

}