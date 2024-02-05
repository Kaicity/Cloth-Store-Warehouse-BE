package com.example.ctcommondal.repository;

import com.example.ctcommondal.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, String> {
    @Query("SELECT f FROM ProductEntity f WHERE f.id = :id ")
    List<ProductEntity> findAllFoodById(@Param("id") String id);

    @Query("SELECT p FROM ProductEntity p WHERE p.code = :code ")
    ProductEntity findProductByCode(@Param("code") String code);

    @Query("SELECT p FROM ProductEntity p WHERE p.name LIKE %:name%")
    List<ProductEntity> findProductByName(@Param("name") String name);
}
