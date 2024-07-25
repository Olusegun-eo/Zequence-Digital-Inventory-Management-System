package com.zequence.ZequenceIms.repository;

import com.zequence.ZequenceIms.prodStocDaosDtos.ProductDao;
import com.zequence.ZequenceIms.prodStocDaosDtos.ProductHistoryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<ProductDao, Long> {

//    @Query("SELECT p FROM ProductDao p WHERE p.id = :productCode")
//    ProductDao findProductByProductCode(@Param("productCode") String productCode);
    //void deleteProductByProductCode(String productCode);

    @Query("SELECT ph FROM ProductHistory ph WHERE ph.productId = :productId")
    List<ProductHistoryDao> findProductHistoryById(@Param("productId") Long productId);
}
