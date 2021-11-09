package v.kiselev.services;

import java.util.Optional;

public interface PictureService {

    Optional<String> getPictureContentTypeById(long id);

    Optional<byte[]> getPictureDataById(long id);

    String createPicture(byte[] picture);

    void deletePicture(long id);

    Long findProductByPictureId(Long pictureId);
}
