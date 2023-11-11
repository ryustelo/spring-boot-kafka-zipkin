package ryl.spring.boot.kafka.zipkin.categorization.model;

import java.util.Arrays;

enum CategoryType {
    ONE_OFF_PAYMENT(0),
    RECURRING_PAYMENT(1),
    REFUND(2);

    final int modulo;

    CategoryType(int modulo) {
        this.modulo = modulo;
    }

    static CategoryType valueByModulo(int modulo) {
        return Arrays.stream(CategoryType.values())
                .filter(c -> modulo == c.modulo)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Incorrect transaction category"));
    }
}
