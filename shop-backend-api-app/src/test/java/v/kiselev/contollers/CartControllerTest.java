package v.kiselev.contollers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import v.kiselev.controllers.DTO.AddLineItemDto;
import v.kiselev.controllers.DTO.CategoryDto;
import v.kiselev.controllers.DTO.ProductDto;
import v.kiselev.services.CartService;
import v.kiselev.services.ProductService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class CartControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SimpMessagingTemplate template;


    @Test
    public void TestAddToCart() throws Exception {
//        List<Long> pictures = new ArrayList<>();
//        pictures.add(1L);
//        pictures.add(2L);
//        ProductDto productDto= new ProductDto(1L, "Apple", "Apple 12",
//                new BigDecimal(80000),
//                new CategoryDto(1L, "phone"),
//                pictures);


        AddLineItemDto addLineItemDto = new AddLineItemDto();
        addLineItemDto.setProductId(1L);
        addLineItemDto.setQty(1);
        addLineItemDto.setColor("black");
        addLineItemDto.setMaterial("glass");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson= null;
        try {
            requestJson = ow.writeValueAsString(addLineItemDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        //Forbidden, для добавления доступ должен быть
        mvc.perform(MockMvcRequestBuilders
                        .post("/cart")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(addLineItemDto.getProductId())));
    }

}
