package com.tgrajkowski.validator;

import com.tgrajkowski.model.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class AgeValidator {
    private final PeselDateExtractor peselDateExtractor;
    private final Clock clock;
    private static final Long LEGAL_AGE = 18L;

    public boolean isPersonAdult(AccountDto accountDto) {
        LocalDate dateOfBirth = peselDateExtractor.getDateOfBirthFromPesel(accountDto.getPesel());
        LocalDate currentDate = LocalDate.now(clock);
        long years = ChronoUnit.YEARS.between(dateOfBirth, currentDate);
        return years >= LEGAL_AGE;
    }
}
