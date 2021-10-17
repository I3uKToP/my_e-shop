package v.kiselev.services;

import org.springframework.stereotype.Service;
import v.kiselev.persist.BrandRepository;
import v.kiselev.persist.model.Brand;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }


    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }


    @Override
    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        brandRepository.deleteById(id);
    }

    @Override
    public void save(Brand brand) {
        brandRepository.save(brand);
    }
}
