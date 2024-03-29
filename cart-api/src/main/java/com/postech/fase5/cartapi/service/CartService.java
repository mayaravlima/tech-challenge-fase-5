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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartService {

    private static final String PRODUCT_SERVICE_URL = "http://PRODUCT-API/products/%s";

    @Qualifier("webclient")
    private final WebClient.Builder webBuilder;
    private final CartRepository cartRepository;
    private final ObjectMapper objectMapper;

    public CartResponse processAddRequest(String cartId, List<CartRequest> shoppingCartRequestList, Long userId) {
        return getProductDetails(shoppingCartRequestList)
                .flatMap(product -> Flux.fromIterable(shoppingCartRequestList)
                        .filter(request -> request.getProductId().equals(product.getId()))
                        .map(request -> updateProductQuantity(product, request.getQuantity())))
                .collectList()
                .flatMap(productList -> {
                    double totalCost = getTotalCost(productList);
                    Integer totalItems = totalItems(productList);

                    var optionalCart = cartRepository.findByCartId(cartId);

                    if (optionalCart.isPresent()){
                        Cart cartEntity = optionalCart.get();
                        cartEntity.setTotalItems(totalItems);
                        cartEntity.setTotalCost(totalCost);
                        cartEntity.setProducts(serializeProducts(productList));
                        return Mono.fromCallable(() -> cartRepository.save(cartEntity))
                                .map(cart -> buildCartResponse(cart, productList));
                    }

                    Cart cartEntity = createCartEntity(cartId, totalItems, totalCost, productList, userId);
                    return Mono.fromCallable(() -> cartRepository.save(cartEntity))
                            .map(cart -> buildCartResponse(cart, productList));
                }).block();
    }

    public CartResponse getCart(String cartId) {
        Cart cartEntity = cartRepository.findByCartId(cartId).orElseThrow(
                () -> new CartException("Cart not found", HttpStatus.NOT_FOUND.value())
        );
        List<Product> products = deserializeProducts(cartEntity.getProducts());
        return buildCartResponse(cartEntity, products);
    }

    public CartResponse removeItemFromCart(String cartId, Long productId) {
        Cart cartEntity = cartRepository.findByCartId(cartId).orElseThrow(
                () -> new CartException("Cart not found", HttpStatus.NOT_FOUND.value())
        );
        List<Product> products = deserializeProducts(cartEntity.getProducts());
        products.removeIf(product -> product.getId().equals(productId));
        cartEntity.setProducts(serializeProducts(products));
        cartEntity.setTotalItems(totalItems(products));
        cartEntity.setTotalCost(getTotalCost(products));
        cartEntity = cartRepository.save(cartEntity);
        return buildCartResponse(cartEntity, products);
    }

    public CartResponse addItemToCart(String cartId, CartRequest cartRequest) {
        return getProductDetails(cartRequest.getProductId())
                .flatMap(product -> {
                    product.setQuantity(cartRequest.getQuantity());
                    Cart cartEntity = cartRepository.findByCartId(cartId).orElseThrow(
                            () -> new CartException("Cart not found", HttpStatus.NOT_FOUND.value())
                    );
                    List<Product> products = deserializeProducts(cartEntity.getProducts());
                    List<Product> newList = addOrUpdateProduct(products, product);
                    cartEntity.setProducts(serializeProducts(newList));
                    cartEntity.setTotalItems(totalItems(newList));
                    cartEntity.setTotalCost(getTotalCost(newList));
                    cartEntity = cartRepository.save(cartEntity);
                    return Mono.just(buildCartResponse(cartEntity, products));
                }).block();
    }

    public void clearCart(String cartId) {
        Cart cartEntity = cartRepository.findByCartId(cartId).orElseThrow(
                () -> new CartException("Cart not found", HttpStatus.NOT_FOUND.value())
        );
        cartEntity.setProducts(serializeProducts(new ArrayList<>()));
        cartEntity.setTotalItems(0);
        cartEntity.setTotalCost(0.0);
        cartEntity = cartRepository.save(cartEntity);
        buildCartResponse(cartEntity, new ArrayList<>());
    }

    private Mono<Product> getProductDetails(Long productId) {
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

    private Cart createCartEntity(String cartId, int totalItems, double totalCost, List<Product> products, Long userId) {
        String serializedProducts = serializeProducts(products);
        return Cart.builder()
                .cartId(cartId)
                .userId(userId)
                .totalItems(totalItems)
                .totalCost(totalCost)
                .products(serializedProducts)
                .build();
    }

    public CartResponse buildCartResponse(Cart cart, List<Product> products) {
        return CartResponse.builder()
                .id(cart.getId())
                .cartId(cart.getCartId())
                .userId(cart.getUserId())
                .totalItems(cart.getTotalItems())
                .totalCost(cart.getTotalCost())
                .products(products)
                .build();
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

    public List<Product> addOrUpdateProduct(List<Product> productList, Product newProduct) {
        boolean existingProduct = false;

        for (Product product : productList) {
            if (Objects.equals(product.getId(), newProduct.getId())) {
                product.setQuantity(product.getQuantity() + newProduct.getQuantity());
                existingProduct = true;
                break;
            }
        }

        if (!existingProduct) {
            productList.add(newProduct);
        }

        return productList;
    }

    public double getTotalCost(List<Product> products) {
        return products.stream()
                .mapToDouble(product -> product.getAmount() * product.getQuantity())
                .reduce(0.0, Double::sum);
    }

    public int totalItems(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
    }

}