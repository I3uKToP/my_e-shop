package v.kiselev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import v.kiselev.services.PictureService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping()
public class PictureController {

    private PictureService pictureService;

    @Autowired
    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping("/{pictureId}")
    public void downloadProductPicture(@PathVariable("pictureId") Long pictureId, HttpServletResponse response) throws IOException {
        Optional<String> opt = pictureService.getPictureContentTypeById(pictureId);

        if(opt.isPresent()){
            response.setContentType(opt.get());
            response.getOutputStream().write(pictureService.getPictureDataById(pictureId).get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @DeleteMapping("/{pictureId}")
    public String deletePicture(@PathVariable("pictureId") Long pictureId) {

        Long productId = pictureService.findProductByPictureId(pictureId);

        pictureService.deletePicture(pictureId);


        return "redirect:/product/".concat(String.valueOf(productId));
    }
}
