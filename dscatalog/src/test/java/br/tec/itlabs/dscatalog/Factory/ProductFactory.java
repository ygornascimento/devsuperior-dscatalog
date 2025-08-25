package br.tec.itlabs.dscatalog.Factory;

import br.tec.itlabs.dscatalog.dto.ProductDTO;
import br.tec.itlabs.dscatalog.entities.Category;
import br.tec.itlabs.dscatalog.entities.Product;

import java.time.Instant;

public class ProductFactory {

    public static Product createProduct() {
        Product product = new Product(
                1L,
                "Phone",
                "Good Phone",
                800.0,
                "https://img.com/img.png",
                Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO crateProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
