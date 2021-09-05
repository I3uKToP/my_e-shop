package v.kiselev.persist;

import org.springframework.data.jpa.repository.JpaRepository;
import v.kiselev.persist.model.Picture;

public interface PictureRepository extends JpaRepository<Picture, Long> {
}
