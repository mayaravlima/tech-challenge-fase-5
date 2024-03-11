package com.postech.fase5.cartapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.postech.fase5.cartapi.entity.Cart;
import com.postech.fase5.cartapi.exception.CartException;
import com.postech.fase5.cartapi.model.CartRequest;
import com.postech.fase5.cartapi.model.CartResponse;
import com.postech.fase5.cartapi.model.Product;
import com.postech.fase5.cartapi.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartService {

    private static final String PRODUCT_SERVICE_URL = "http://localhost:8081/products/%s";

    private final WebClient.Builder webBuilder;
    private final CartRepository cartRepository;
    private final ObjectMapper objectMapper;

    public CartResponse processAddRequest(Long userId, List<CartRequest> shoppingCartRequestList) {
        return getProductDetails(shoppingCartRequestList)
                .flatMap(product -> Flux.fromIterable(shoppingCartRequestList)
                        .filter(request -> request.getProductId().equals(product.getId()))
                        .map(request -> updateProductQuantity(product, request.getQuantity())))
                .collectList()
                .flatMap(productList -> {
                    double totalCost = productList.stream().mapToDouble(Product::getAmount).sum() * shoppingCartRequestList.size();
                    Cart cartEntity = createCartEntity(userId, productList.size(), totalCost, productList);
                    return Mono.fromCallable(() -> cartRepository.save(cartEntity))
                            .map(cart -> buildCartResponse(cart, productList));
                }).block();
    }

    public CartResponse getCart(Long userId) {
        Cart cartEntity = cartRepository.findByUserId(userId);
        List<Product> products = deserializeProducts(cartEntity.getProducts());
        return buildCartResponse(cartEntity, products);
    }

    public CartResponse removeItemFromCart(Long userId, Long productId) {
        Cart cartEntity = cartRepository.findByUserId(userId);
        List<Product> products = deserializeProducts(cartEntity.getProducts());
        products.removeIf(product -> product.getId().equals(productId));
        cartEntity.setProducts(serializeProducts(products));
        cartEntity = cartRepository.save(cartEntity);
        return buildCartResponse(cartEntity, products);
    }

    public CartResponse addItemToCart(Long userId, CartRequest cartRequest) {
        return getProductDetails(cartRequest.getProductId())
                .flatMap(product -> {
                    product.setQuantity(cartRequest.getQuantity());
                    Cart cartEntity = cartRepository.findByUserId(userId);
                    List<Product> products = deserializeProducts(cartEntity.getProducts());
                    products.add(product);
                    cartEntity.setProducts(serializeProducts(products));
                    cartEntity = cartRepository.save(cartEntity);
                    return Mono.just(buildCartResponse(cartEntity, products));
                }).block();
    }

    public void clearCart(Long userId) {
        Cart cartEntity = cartRepository.findByUserId(userId);
        cartEntity.setProducts(serializeProducts(new ArrayList<>()));
        cartEntity.setTotalItems(0);
        cartEntity.setTotalCost(0.0);
        cartEntity = cartRepository.save(cartEntity);
        buildCartResponse(cartEntity, new ArrayList<>());
    }

    private Mono<Product> getProductDetails(Long productId) {
       var test = webBuilder.build()
               .get()
               .uri(String.format(PRODUCT_SERVICE_URL, productId))
               .retrieve()
               .bodyToMono(Product.class);
        return webBuilder.build()
                .get()
                .uri(String.format(PRODUCT_SERVICE_URL, productId))
                .retrieve()
                .bodyToMono(Product.class);
    }

    private Flux<Product> getProductDetails(List<CartRequest> requests) {
        return Flux.fromIterable(requests)
                .flatMap(request -> getProductDetails(request.getProductId()));
    }

    private Product updateProductQuantity(Product product, Integer quantity) {
        product.setQuantity(quantity);
        return product;
    }

    private Cart createCartEntity(Long userId, int totalItems, double totalCost, List<Product> products) {
        String serializedProducts = serializeProducts(products);
        Long cartId = generateCartId();
        return Cart.builder()
                .userId(userId)
                .cartId(cartId)
                .totalItems(totalItems)
                .totalCost(totalCost)
                .products(serializedProducts)
                .build();
    }


    public CartResponse buildCartResponse(Cart cart, List<Product> products) {
        return CartResponse.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUserId())
                .totalItems(cart.getTotalItems())
                .totalCost(cart.getTotalCost())
                .products(products)
                .build();
    }

    private Long generateCartId() {
        return (long) (Math.random() * Math.pow(10, 10));
    }

    private String serializeProducts(List<Product> products) {
        try {
            return objectMapper.writeValueAsString(products);
        } catch (JsonProcessingException e) {
            throw new CartException(e.getMessage());
        }
    }

    private List<Product> deserializeProducts(String products) {
        try {
            return objectMapper.readValue(products, new TypeReference<List<Product>>() {});
        } catch (JsonProcessingException e) {
            throw new CartException(e.getMessage());
        }
    }

}