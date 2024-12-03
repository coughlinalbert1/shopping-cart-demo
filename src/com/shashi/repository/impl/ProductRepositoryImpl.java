package com.shashi.repository.impl;

/**
 * Implementation of the ProductRepository interface.
 * Handles database operations for the Product entity using JDBC.
 */
import com.shashi.beans.ProductBean;
import com.shashi.repository.ProductRepository;
import com.shashi.utility.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepositoryImpl implements ProductRepository {
    @Override
    public boolean save(ProductBean product) {
        try (Connection con = DBUtil.provideConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO product VALUES (?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, product.getProdId());
            ps.setString(2, product.getProdName());
            ps.setString(3, product.getProdType());
            ps.setString(4, product.getProdInfo());
            ps.setDouble(5, product.getProdPrice());
            ps.setInt(6, product.getProdQuantity());
            ps.setBlob(7, product.getProdImage());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
