package v.kiselev.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<v.kiselev.persist.User, Long>, JpaSpecificationExecutor<v.kiselev.persist.User> {

    List<v.kiselev.persist.User> findByUsernameStartsWith(String prefix);

    @Query("select u " +
            "from User u " +
            "where ( u.username like CONCAT(:prefix, '%') or :prefix is null ) and " +
            "( u.age >= :minAge or :minAge is null ) and " +
            "( u.age <= :maxAge or :maxAge is null )")
    List<v.kiselev.persist.User> filterUsers(@Param("prefix") String prefix,
                                             @Param("minAge") Integer minAge,
                                             @Param("maxAge") Integer maxAge);


    @Query("select distinct u " +
            "from User u " +
            "left join fetch u.roles " +
            "where u.username = :username")
    Optional<v.kiselev.persist.User> findByUsername(@Param("username") String username);
}
