package br.tec.itlabs.dscatalog.resources;

import br.tec.itlabs.dscatalog.entities.Category;
import br.tec.itlabs.dscatalog.services.CateroryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResources  {

    private final CateroryService cateroryService;

    public CategoryResources(CateroryService cateroryService) {
        this.cateroryService = cateroryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        List<Category> list = cateroryService.findAll();
        return ResponseEntity.ok().body(list);
    }
}
