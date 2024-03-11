package com.postech.fase5.productsapi.service;

import com.postech.fase5.productsapi.model.ProductRequest;
import com.postech.fase5.productsapi.model.ProductResponse;
import com.postech.fase5.productsapi.entity.Product;
import com.postech.fase5.productsapi.exception.ProductServiceCustomException;
import com.postech.fase5.productsapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponse addProduct(ProductRequest productRequest) {
        log.info("Adding product: {}", productRequest);

        Product product = Product.builder()
                .name(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .amount(productRequest.getAmount())
                .build();

        product = productRepository.save(product);

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .quantity(product.getQuantity())
                .amount(product.getAmount())
                .build();
    }

    public List<ProductResponse> addAllProducts (List<ProductResponse> products) {
        log.info("Adding products: {}", products);
        List<Product> productList = products.stream()
                .map(product -> Product.builder()
                        .name(product.getName())
                        .quantity(product.getQuantity())
                        .amount(product.getAmount())
                        .build())
                .toList();

        productList = productRepository.saveAll(productList);

        return productList.stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .quantity(product.getQuantity())
                        .amount(product.getAmount())
                        .build())
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        log.info("Getting product by id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductServiceCustomException("Product not found",
                                HttpStatus.NOT_FOUND.value()));

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .quantity(product.getQuantity())
                .amount(product.getAmount())
                .build();
    }

    public List<ProductResponse> getAllProducts() {
        log.info("Getting all products");
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            throw new ProductServiceCustomException("No products found",
                    HttpStatus.NOT_FOUND.value());
        }

        return products.stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .quantity(product.getQuantity())
                        .amount(product.getAmount())
                        .build())
                .toList();
    }

    public void deleteProductById(Long id) {
        log.info("Deleting product by id: {}", id);
        productRepository.deleteById(id);
    }

    public ProductResponse updateProductById(Long id, ProductRequest productRequest) {
        log.info("Updating product by id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductServiceCustomException("Product not found",
                                HttpStatus.NOT_FOUND.value()));

        product.setName(productRequest.getName());
        product.setQuantity(productRequest.getQuantity());
        product.setAmount(productRequest.getAmount());

        product = productRepository.save(product);

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .quantity(product.getQuantity())
                .amount(product.getAmount())
                .build();
    }



}
