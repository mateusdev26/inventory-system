package main.service;

import lombok.extern.log4j.Log4j2;
import main.domain.Product;
import main.repository.ProductRepository;

import java.util.List;

@Log4j2
public final class ProductService {

    public static void save(Product product) {
        if (product == null) {
            log.error("Product can't be null");
        } else {
            log.info("Rows affected : '" + new ProductRepository().save(product) + "'");
        }
    }


    public static void delete(Number id) {
        if (id.intValue() <= 0) {
            log.error("Id can't be '0' or negative");
        } else {
            log.info("Rows affected : '" + new ProductRepository().delete(id) + "'");
        }
    }


    public static void delete(Number id, int limit) {
        if (limit == 0) {
            delete(id);
            return;
        }
        if (id.intValue() == 0) {
            log.error("Id can't be '0'");
        } else {
            log.info("Rows affected : '" + new ProductRepository().delete(id, limit) + "'");
        }
    }


    public static void delete(Number... ids) {
        if (ids.length == 0) {
            log.error("Ids can't be empty");
        } else {
            log.info("Rows affected : '" + new ProductRepository().delete(ids) + "'");
        }
    }


    public static void delete(int limit, Number... ids) {
        if (limit == 0) {
            delete(ids);
        }
        if (ids.length == 0) {
            log.error("Ids can't be empty");
        } else {
            log.info("Rows affected : '" + new ProductRepository().delete(limit, ids) + "'");
        }
    }

    public static void update(Number id, Product product) {
        if (id.intValue() == 0) {
            log.error("Id can't be '0'");
        }
        if (product == null) {
            log.error("Product can't be null");
        }
        if (id.intValue() > 0 && product != null) {
            log.info("Rows affected : '" + new ProductRepository().update(id, product) + "'");
        }
    }

    public static void update(Number id, Product product, int limit) {
        if (id.intValue() == 0) {
            log.error("Id can't be '0'");
        }
        if (product == null) {
            log.error("Product can't be null");
        }
        if (limit <= 0) {
            log.error("Limit can't be '0' or negative");
        }
        if (id.intValue() > 0 && product != null && limit > 0) {
            log.info("Rows affected : '" + new ProductRepository().update(id, product, limit) + "'");
        }
    }

    public static List<Product> findAll() {
        List<Product> all = new ProductRepository().findAll();
        if (all.isEmpty()) {
            log.error("No products found");
            return List.of();
        }
        return all;

    }

    public static Product findById(Number id) {
        Product found = new ProductRepository().findById(id);
        if (found == null) {
            log.error("No product found by id");
            return null;
        }
        return found;
    }
}
