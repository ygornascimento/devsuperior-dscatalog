package br.tec.itlabs.dscatalog.repositories;

import br.tec.itlabs.dscatalog.Factory.ProductFactory;
import br.tec.itlabs.dscatalog.entities.Product;
import br.tec.itlabs.dscatalog.repository.ProductRepository;
import com.sun.istack.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    long existingId;
    long unexistId;
    private long countTotalProducts;

    @BeforeEach
    void init() {
        existingId = 1L;
        unexistId = 101L;
        countTotalProducts = 25L;
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Product product = ProductFactory.createProduct();
        product.setId(null);
        repository.save(product);

        assertNotNull(product.getId());
        assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExits() {

        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);

        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnOptionalProductEmptyWhenIdExists() {
        Optional<Product> result = repository.findById(existingId);
        assertTrue(result.isPresent());
    }

    @Test
    public void shouldNotReturnOptionalProductEmptyWhenIdUnexists() {
        Optional<Product> result = repository.findById(unexistId);
        assertFalse(result.isPresent());
    }
}
