package v.kiselev.persist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import v.kiselev.persist.model.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

//    @Query(value = "select p from Product p left join fetch p.category",
//    countQuery = "select count(p) from Product p")
    @EntityGraph("product-wth-all")
    Page<Product> findAll(Pageable var2);

    Optional<Product> findByName(String name);

}
