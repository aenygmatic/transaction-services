package com.virtualbank.transaction;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

import java.time.Instant;

public class TransactionMatchers {

    public static Matcher<String> atSameTime(Instant operand) {
        //TODO: take care of non 'Z' timezones
        return new IsEqual<>(operand.toString().substring(0, 22)) {
            @Override
            public boolean matches(Object actualValue) {
                return super.matches(actualValue.toString().substring(0, 22));
            }
        };
    }
}
