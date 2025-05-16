package store.product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductResource implements ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductResource.class.getName());

    @Autowired
    private ProductService productService;

    @Override
    public ResponseEntity<ProductOut> create(ProductIn productIn) {
        logger.debug("Creating product: " + productIn);
        Product created = productService.create(ProductParser.to(productIn));
        return ResponseEntity.ok().body(ProductParser.to(created));
    }

    @Override
    public ResponseEntity<List<ProductOut>> findAll() {
        return ResponseEntity
            .ok()
            .body(productService.findAll()
                .stream()
                .map(ProductParser::to)
                .toList());
    }

    @Override
    public ResponseEntity<ProductOut> findById(String id) {
        return ResponseEntity
            .ok()
            .body(ProductParser.to(productService.findById(id)));
    }

    @Override
    public ResponseEntity<ProductOut> deleteById(String id) {
        return ResponseEntity
            .ok()
            .body(ProductParser.to(productService.deleteById(id)));
    }
}
