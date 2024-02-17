package com.example.ctcommondal.repository;

import com.example.ctcommon.enums.StatusSupplier;
import com.example.ctcommondal.entity.CustomerEntity;
import com.example.ctcommondal.entity.OptionProductEntity;
import com.example.ctcommondal.entity.SupplierEntity;
import jdk.jshell.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISupplierRepository extends JpaRepository<SupplierEntity, String> {
        @Query("SELECT s FROM SupplierEntity s WHERE s.status = :status")
        List<SupplierEntity> findAllSupplierStatus(@Param("status")StatusSupplier status);

        @Query("SELECT c FROM SupplierEntity c WHERE c.id = :id")
        SupplierEntity findSupplierEntitiesById(@Param("id") String id);
}
