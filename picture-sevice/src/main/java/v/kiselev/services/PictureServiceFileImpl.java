package v.kiselev.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import v.kiselev.persist.PictureRepository;
import v.kiselev.persist.model.Picture;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class PictureServiceFileImpl implements PictureService {

    public static final Logger logger = LoggerFactory.getLogger(PictureServiceFileImpl.class);

    @Value("${picture.storage.path}")
    private String storagePath;

    private final PictureRepository pictureRepository;

    @Autowired
    public PictureServiceFileImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public Optional<String> getPictureContentTypeById(long id) {
        return pictureRepository.findById(id).map(Picture::getContentType);
    }

    @Override
    public Optional<byte[]> getPictureDataById(long id) {
        return pictureRepository.findById(id)
                .map(pic-> Paths.get(storagePath, pic.getStorageId()))
                .filter(Files::exists)
                .map(path -> {
                    try {
                        return Files.readAllBytes(path);
                    } catch (IOException ex) {
                        logger.error("Can't read file for picture with id " + id + " files", ex);
                        throw new RuntimeException(ex);
                    }
                });
    }

    @Override
    public String createPicture(byte[] picture) {
        String fileName = UUID.randomUUID().toString().concat(".jpeg");
        try(OutputStream os = Files.newOutputStream((Paths.get(storagePath, fileName)))) {

            os.write(picture);

        } catch (IOException ex) {
            logger.error("Can't write file", ex);
            throw new RuntimeException(ex);
        }
        return fileName;
    }

    @Override
    @Transactional
    public void deletePicture(long id) {
        logger.info("try to delete pictures");
        String fileName = pictureRepository.findById(id).get().getStorageId();
        try {
            Files.delete(Paths.get(storagePath, fileName));
        } catch (IOException ex) {
            logger.error("Can't delete file", ex);
            throw new RuntimeException(ex);
        }
        pictureRepository.deleteById(id);
    }

    @Override
    public Long findProductByPictureId(Long pictureId) {
        return pictureRepository.findById(pictureId).get().getProduct().getId();
    }
}
