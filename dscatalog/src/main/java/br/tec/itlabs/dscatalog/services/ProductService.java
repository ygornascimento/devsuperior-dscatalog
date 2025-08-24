package br.tec.itlabs.dscatalog.services;

import br.tec.itlabs.dscatalog.dto.CategoryDTO;
import br.tec.itlabs.dscatalog.dto.ProductDTO;
import br.tec.itlabs.dscatalog.entities.Category;
import br.tec.itlabs.dscatalog.entities.Product;
import br.tec.itlabs.dscatalog.repository.CategoryRepository;
import br.tec.itlabs.dscatalog.repository.ProductRepository;
import br.tec.itlabs.dscatalog.services.exceptions.DatabaseException;
import br.tec.itlabs.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> list = productRepository.findAll(pageRequest);
        return list.map(x -> new ProductDTO(x));

//        List<ProductDTO> listDTO = new ArrayList<>();
//        for (Product Product: list) {
//            listDTO.add(new ProductDTO(Product));
//        }
//        return listDTO;
    }

    @Transactional
    public ProductDTO findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found..."));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        try {
            Product entity = productRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e ) {
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado.");
        }
        try {
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial.");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();

        for(CategoryDTO catDTO: dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(catDTO.getId());
            entity.getCategories().add(category);
        }
    }

}
