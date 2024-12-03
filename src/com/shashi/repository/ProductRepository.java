package com.shashi.repository;

/**
 * Repository interface for performing CRUD operations on Product entities.
 * Defines methods for saving, finding, updating, and deleting products in the database.
 * Single Responsibility is to manage products in database
 */
import com.shashi.beans.ProductBean;
import java.util.List;

public interface ProductRepository {
    boolean save(ProductBean product);
}
