package ryl.spring.boot.kafka.zipkin.categorization.model;

public interface Categorizable {

    CategoryType calculateCategory();
}
