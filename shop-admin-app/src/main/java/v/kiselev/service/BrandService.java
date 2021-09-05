package v.kiselev.service;

import v.kiselev.persist.model.Brand;

import java.util.List;
import java.util.Optional;

public interface BrandService {

    List<Brand> findAll();

    Optional<Brand> findById(Long id);

    void deleteById(Long id);

    void save(Brand brand);
}
