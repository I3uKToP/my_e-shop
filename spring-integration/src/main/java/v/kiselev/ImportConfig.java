package v.kiselev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.ConsumerEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.dsl.JpaUpdatingOutboundEndpointSpec;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.messaging.MessageHandler;
import v.kiselev.persist.BrandRepository;
import v.kiselev.persist.CategoryRepository;
import v.kiselev.persist.ProductRepository;
import v.kiselev.persist.model.Brand;
import v.kiselev.persist.model.Category;
import v.kiselev.persist.model.Product;

import javax.persistence.EntityManagerFactory;
import java.io.File;
import java.math.BigDecimal;

@Configuration
public class ImportConfig {

    private static final Logger logger = LoggerFactory.getLogger(ImportConfig.class);

    @Value("${source.directory.path}")
    private String sourceDirPath;

    @Value("${dest.directory.path}")
    private String destDirPath;

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public ImportConfig(BrandRepository brandRepository, CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Bean
    public MessageSource<File> sourceDirectory() {
        FileReadingMessageSource messageSource = new FileReadingMessageSource();
        messageSource.setDirectory(new File(sourceDirPath));
        messageSource.setAutoCreateDirectory(true);
        return messageSource;
    }

    @Bean
    public MessageHandler destDirectory() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File(destDirPath));
        handler.setExpectReply(false);
        handler.setDeleteSourceFiles(true);
        return handler;
    }

    @Bean
    public JpaUpdatingOutboundEndpointSpec jpaPersistHandler() {
        return Jpa.outboundAdapter(this.entityManagerFactory)
                .entityClass(Product.class)
                .persistMode(PersistMode.PERSIST);
    }

    @Bean
    public IntegrationFlow createProducts() {
        return IntegrationFlows.from(sourceDirectory(), conf -> conf.poller(Pollers.fixedDelay(2000)))
                .filter(msg -> ((File) msg).getName().endsWith(".csv"))
                .transform(new FileToStringTransformer())
                .split(s -> s.delimiters("\n"))
                .<String, Product>transform(str -> {
                    logger.info("new str {}", str);
                    String[] properties = str.split(";");
                    if(productRepository.findByName(properties[0]).isPresent()) {
                        return productRepository.findByName(properties[0]).get();
                    }
                    Product product = new Product();
                    product.setName(properties[0].trim());
//                    logger.info("Name " + properties[0]);
                    product.setPrice(new BigDecimal(properties[1].trim()));
//                    logger.info("Price " + properties[1]);
                    product.setDescription(properties[2]);
//                    logger.info("Desc " + properties[2]);

                    if(!brandRepository.findByName(properties[4].trim()).isPresent()) {
                        logger.info("brand not found");
                        logger.info("Create new brand name: " + properties[4]);
                        brandRepository.save(new Brand(properties[4].trim()));
                    }
                    Brand brand = brandRepository.findByName(properties[4].trim()).get();
                    logger.info("Brand " + brand.getName());
                    product.setBrand(brand);

                    if(!categoryRepository.findByName(properties[3].trim()).isPresent()) {
                        logger.info("brand not found");
                        logger.info("Create new brand name: " + properties[3]);
                        categoryRepository.save(new Category(properties[3].trim()));
                    }
                    Category category = categoryRepository.findByName(properties[3]).get();
                    product.setCategory(category);
                    logger.info("brand from properties: " + properties[4]);
                    return product;
                })
                .handle(jpaPersistHandler(), ConsumerEndpointSpec::transactional)
                .get();

    }

//    @Bean
//    public IntegrationFlow fileMoveFlow() {
//        return IntegrationFlows.from(sourceDirectory(), conf -> conf.poller(Pollers.fixedDelay(2000)))
//                .filter(msg -> ((File) msg).getName().endsWith(".txt"))
//                .transform(new FileToStringTransformer())
//                .<String, String>transform(String::toUpperCase)
//                .handle(destDirectory())
//                .get();
//
//    }
}
