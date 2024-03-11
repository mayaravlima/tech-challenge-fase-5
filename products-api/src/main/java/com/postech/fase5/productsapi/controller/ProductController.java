package com.postech.fase5.productsapi.controller;

import com.postech.fase5.productsapi.model.ProductRequest;
import com.postech.fase5.productsapi.model.ProductResponse;
import com.postech.fase5.productsapi.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> addProducts(@RequestBody ProductRequest product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(product));
    }

    @PostMapping("/all")
    public ResponseEntity<List<ProductResponse>> addAllProducts(@RequestBody List<ProductResponse> products) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addAllProducts(products));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest product) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.updateProductById(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
