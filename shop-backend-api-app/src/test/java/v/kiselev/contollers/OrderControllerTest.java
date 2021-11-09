package v.kiselev.contollers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import v.kiselev.controllers.DTO.CategoryDto;
import v.kiselev.controllers.DTO.OrderDto;
import v.kiselev.controllers.DTO.ProductDto;
import v.kiselev.persist.*;
import v.kiselev.persist.model.*;
import v.kiselev.services.CartService;
import v.kiselev.services.OrderService;
import v.kiselev.services.OrderServiceImpl;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class OrderControllerTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private SimpMessagingTemplate template;

    @BeforeEach
    public void init() {
        orderService = new OrderServiceImpl(orderRepository,
                cartService, userRepository,
                productRepository, rabbitTemplate, template);
    }

    @Test
    public void testFindAllUnauthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/order/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(value = "admin", password = "123", roles = {"ADMIN"})
    @Test
    public void findOrdersByUser() {
        Set<Role> roles = new HashSet<>();
        Role role = new Role(1L, "ADMIN");
        roles.add(role);
        roleRepository.save(role);

        userRepository.save(new User(1L, "admin", "123", 28, roles));
        Brand brand = new Brand("apple");
        brandRepository.save(brand);
        Category phone = new Category("phone");
        categoryRepository.save(phone);
        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setPrice(new BigDecimal(80));
        expectedProduct.setName("Apple");
        expectedProduct.setId(1L);
        expectedProduct.setCategory(new CategoryDto(1L, "phone"));
        productRepository.save(new Product(expectedProduct.getName(),
                expectedProduct.getPrice(),
                "desc",
                phone,
                brand));

        cartService.addProductQty(expectedProduct, "color", "material", 2);

        orderService.createOrder("admin");


        List<OrderDto> order = orderService.findOrdersByUsername("admin");
        assertEquals("admin", order.get(0).getUsername());
        assertEquals(1, order.size());
        assertEquals("Apple", order.get(0).getOrderLineItems().get(0).getProductName());
    }

    @WithMockUser(value = "user", password = "123", roles = {"USER"})
    @Test
    public void findOrdersByUser2() {


        //for User
        Set<Role> roles1 = new HashSet<>();
        Role role1 = new Role(1L, "USER");
        roles1.add(role1);
        roleRepository.save(role1);

        userRepository.save(new User(2L, "user", "123", 50, roles1));
        Brand brand1 = new Brand("dell");
        brandRepository.save(brand1);
        Category computer = new Category("computer");
        categoryRepository.save(computer);
        ProductDto expectedProduct2 = new ProductDto();
        expectedProduct2.setPrice(new BigDecimal(90000));
        expectedProduct2.setName("Dell");
        expectedProduct2.setId(2L);
        expectedProduct2.setCategory(new CategoryDto(2L, "computer"));
        productRepository.save(new Product(expectedProduct2.getName(),
                expectedProduct2.getPrice(),
                "desc",
                computer,
                brand1));

        cartService.addProductQty(expectedProduct2, "black", "steal", 1);
        orderService.createOrder("user");

        List<OrderDto> order = orderService.findOrdersByUsername("user");
        assertEquals("user", order.get(0).getUsername());
        assertEquals(1, order.size());
        assertEquals("Dell", order.get(0).getOrderLineItems().get(0).getProductName());
    }



    @WithMockUser(value = "admin", password = "123", roles = {"ADMIN"})
    @Test
    public void testFindAllAuthorized() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/order/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
