package v.kiselev.service;

import v.kiselev.persist.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findAll();

    Optional<Category> findById(Long id);

    void deleteById(Long id);

    void save(Category category);
}
