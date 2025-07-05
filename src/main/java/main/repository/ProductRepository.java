package main.repository;

import lombok.extern.log4j.Log4j2;
import main.conn.ConnectionFactory;
import main.domain.Product;
import main.interfaces.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Log4j2
public class ProductRepository implements Repository<Product> {

    @Override
    public int save(Product product) {
        String sqlInsert = "INSERT INTO products (productname, price, madeDate, expirationDate, country, amount) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE products SET amount = amount + 1 WHERE productname = ? AND price = ? AND madeDate = ? AND expirationDate = ? AND country = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            if (productExists(conn, product)) {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlUpdate)) {
                    setProductParameters(pstmt, product, false);
                    return pstmt.executeUpdate();
                }
            } else {
                try (PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
                    setProductParameters(pstmt, product, true);
                    return pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
    }

    @Override
    public int delete(Number id) {
        String sqlCheck = "SELECT amount FROM products WHERE id = ?";
        String sqlDelete = "DELETE FROM products WHERE id = ?";
        String sqlUpdate = "UPDATE products SET amount = amount - 1 WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {
                checkStmt.setLong(1, id.longValue());
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int amount = rs.getInt("amount");
                    String sql = (amount == 1) ? sqlDelete : sqlUpdate;

                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setLong(1, id.longValue());
                        int rowsAffected = pstmt.executeUpdate();
                        conn.commit();
                        return rowsAffected;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
        return 0;
    }

    @Override
    public int delete(Number id, int limit) {
        String sqlCheck = "SELECT amount FROM products WHERE id = ?";
        String sqlDelete = "DELETE FROM products WHERE id = ? LIMIT ?";
        String sqlUpdate = "UPDATE products SET amount = amount - 1 WHERE id = ? LIMIT ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {
                checkStmt.setLong(1, id.longValue());
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    int amount = rs.getInt("amount");
                    String sql = (amount <= limit) ? sqlDelete : sqlUpdate;

                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setLong(1, id.longValue());
                        pstmt.setInt(2, limit);
                        int rowsAffected = pstmt.executeUpdate();
                        conn.commit();
                        return rowsAffected;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
        return 0;
    }

    @Override
    public int delete(Number... ids) {
        String sqlCheck = "SELECT amount FROM products WHERE id = ?";
        String sqlDelete = "DELETE FROM products WHERE id = ?";
        String sqlUpdate = "UPDATE products SET amount = amount - 1 WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            conn.setAutoCommit(false);
            int totalRowsAffected = 0;

            for (Number id : ids) {
                try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {
                    checkStmt.setLong(1, id.longValue());
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        int amount = rs.getInt("amount");
                        String sql = (amount == 1) ? sqlDelete : sqlUpdate;

                        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                            pstmt.setLong(1, id.longValue());
                            totalRowsAffected += pstmt.executeUpdate();
                        }
                    }
                }
            }
            conn.commit();
            return totalRowsAffected;
        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
    }

    @Override
    public int delete(int limit, Number... ids) {
        if (ids == null || ids.length < 2) {
            return 0;
        }

        String sql = "UPDATE products SET amount = amount - 1 WHERE id BETWEEN ? AND ? LIMIT ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, ids[0].longValue());
            pstmt.setLong(2, ids[1].longValue());
            pstmt.setInt(3, limit);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return products;
    }

    @Override
    public Product findById(Number id) {
        String sql = "SELECT * FROM products WHERE id = ? LIMIT 1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id.longValue());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

    @Override
    public int update(Number id, Product product, int limit) {
        String sql = "UPDATE products SET productname = ?, price = ?, madeDate = ?, expirationDate = ?, country = ?, amount = ? WHERE id = ? LIMIT ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setProductParameters(pstmt, product, true);
            pstmt.setLong(7, id.longValue());
            pstmt.setInt(8, limit);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
    }

    @Override
    public int update(Number id, Product product) {
        String sql = "UPDATE products SET productname = ?, price = ?, madeDate = ?, expirationDate = ?, country = ?, amount = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setProductParameters(pstmt, product, true);
            pstmt.setLong(7, id.longValue());
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            handleSQLException(e);
            return 0;
        }
    }

    // Métodos auxiliares
    private boolean productExists(Connection conn, Product product) throws SQLException {
        String sql = "SELECT 1 FROM products WHERE productname = ? AND price = ? AND madeDate = ? AND expirationDate = ? AND country = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setProductParameters(pstmt, product, false);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void setProductParameters(PreparedStatement pstmt, Product product, boolean includeAmount) throws SQLException {
        pstmt.setString(1, product.getName());
        pstmt.setDouble(2, product.getPrice());
        pstmt.setDate(3, Date.valueOf(product.getMadeDate()));
        pstmt.setDate(4, Date.valueOf(product.getExpirationDate()));
        pstmt.setString(5, product.getCountry());
        if (includeAmount) {
            pstmt.setInt(6, product.getAmount());
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return Product.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("productname"))
                .price(rs.getDouble("price"))
                .madeDate(rs.getDate("madeDate").toLocalDate())
                .expirationDate(rs.getDate("expirationDate").toLocalDate())
                .country(rs.getString("country"))
                .amount(rs.getInt("amount"))
                .build();
    }

    private void handleSQLException(SQLException e) {
        log.error("SQL Error: " + e.getMessage());
        e.printStackTrace();
    }
}