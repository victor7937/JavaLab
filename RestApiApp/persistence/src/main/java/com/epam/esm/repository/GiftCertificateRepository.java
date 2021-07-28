package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for manipulating certificate data in database
 */
@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long>, PagingAndFilteringCertificateRepository {

    /**
     * Checks if certificate exists and is not deleted
     * @param id - id of certificate
     * @return true if certificates id exists and certificate is not deleted
     */
    Optional<GiftCertificate> findByIdAndDeletedIsFalse(Long id);
}
