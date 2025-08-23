package br.tec.itlabs.dscatalog.services;

import br.tec.itlabs.dscatalog.dto.CategoryDTO;
import br.tec.itlabs.dscatalog.entities.Category;
import br.tec.itlabs.dscatalog.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CateroryService {

    private final CategoryRepository repository;

    public CateroryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());

//        List<CategoryDTO> listDTO = new ArrayList<>();
//        for (Category category: list) {
//            listDTO.add(new CategoryDTO(category));
//        }
//        return listDTO;
    }


    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.get();
        return new CategoryDTO(entity);
    }
}
