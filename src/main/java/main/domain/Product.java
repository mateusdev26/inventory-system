package main.domain;
import lombok.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Builder
@ToString
public class Product {
    private Long id;
    private String name;
    private double price;
    private LocalDate madeDate;
    private LocalDate expirationDate;
    private String country;
    private int amount;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(price, product.price) == 0 && Objects.equals(name, product.name) && Objects.equals(madeDate, product.madeDate) && Objects.equals(expirationDate, product.expirationDate) && Objects.equals(country, product.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, madeDate, expirationDate, country);
    }
}
