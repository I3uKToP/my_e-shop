package v.kiselev.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import v.kiselev.persist.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
