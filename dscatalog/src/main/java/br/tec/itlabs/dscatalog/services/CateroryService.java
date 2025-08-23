package br.tec.itlabs.dscatalog.services;

import br.tec.itlabs.dscatalog.entities.Category;
import br.tec.itlabs.dscatalog.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CateroryService {

    private final CategoryRepository repository;

    public CateroryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> findAll() {
        return repository.findAll();
    }
}
