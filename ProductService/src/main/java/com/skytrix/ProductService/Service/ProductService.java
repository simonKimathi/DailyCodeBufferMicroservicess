package com.skytrix.ProductService.Service;

import com.skytrix.ProductService.Model.ProductRequest;
import com.skytrix.ProductService.Model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
