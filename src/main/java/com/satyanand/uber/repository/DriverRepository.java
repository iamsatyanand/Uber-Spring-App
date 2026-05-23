package com.satyanand.uber.repository;

import com.satyanand.uber.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findById(Integer id);

    Optional<Driver> findByEmail(String email);

    Optional<Driver> findByLicenseNumber(String licenseNumber);

    boolean existsByEmail(String email);

    boolean existsByLicenseNumber(String licenseNumber);

    List<Driver> findByIsAvailableTrue();
}

