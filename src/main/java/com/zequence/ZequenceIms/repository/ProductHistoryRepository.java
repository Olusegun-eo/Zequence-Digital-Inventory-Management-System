package com.zequence.ZequenceIms.repository;

import com.zequence.ZequenceIms.prodStocDaosDtos.ProductHistoryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductHistoryRepository extends JpaRepository<ProductHistoryDao, Long> {
    List<ProductHistoryDao> findByProductId(Long productId);
}