package br.tec.itlabs.dscatalog.services.unit;

import br.tec.itlabs.dscatalog.Factory.ProductFactory;
import br.tec.itlabs.dscatalog.dto.ProductDTO;
import br.tec.itlabs.dscatalog.entities.Product;
import br.tec.itlabs.dscatalog.repository.ProductRepository;
import br.tec.itlabs.dscatalog.services.ProductService;
import br.tec.itlabs.dscatalog.services.exceptions.DatabaseException;
import br.tec.itlabs.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/*
* SpringExtension integrates the Spring TestContext Framework into JUnit 5's Jupiter programming model.
*
@ExtendWith(SpringExtension.class) is a JUnit Jupiter (JUnit 5) annotation used to integrate the Spring TestContext Framework with your tests.
Purpose:

    It enables Spring-specific features and functionalities within your JUnit 5 tests, similar to how @RunWith(SpringJUnit4ClassRunner.class) was used in JUnit 4.
    It allows you to leverage the Spring TestContext Framework's capabilities, such as:
        Application Context Loading: Loading and configuring Spring application contexts for your tests.
        Dependency Injection: Injecting Spring-managed beans into your test instances.
        Transactional Test Execution: Managing transactions for tests involving database operations, automatically rolling back changes after each test method.
        Mocking: Facilitating the use of Spring's testing utilities for mocking and stubbing dependencies.
 * */

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    private long existingId;
    private long unExistId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void init() {
        existingId = 1L;
        unExistId = 100L;
        dependentId = 3L;
        product = ProductFactory.createProduct();
        page = new PageImpl<>(List.of(product));

        when(repository.findById(existingId)).thenReturn(Optional.of(product));

        when(repository.findById(unExistId)).thenReturn(Optional.empty());

        when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        when(repository.existsById(existingId)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        when(repository.existsById(unExistId)).thenReturn(false);
        when(repository.existsById(dependentId)).thenReturn(true);
    }

    @Test
    void deleteShouldDoNothingWhenIdExists() {
        assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        verify(repository, Mockito.times(1)).deleteById(existingId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdNotExist() {
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(unExistId);
        });

        verify(repository).existsById(unExistId);
    }

    @Test
    void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });

        verify(repository).existsById(dependentId);
    }

    @Test
    void findAllPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0,10);
        Page<ProductDTO> result = service.findAllPaged(pageable);

        assertNotNull(result);
        verify(repository, times(1)).findAll(pageable);
    }
}