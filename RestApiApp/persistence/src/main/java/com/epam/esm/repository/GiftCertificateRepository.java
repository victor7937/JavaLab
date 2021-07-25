package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for manipulating certificate data in database
 */
@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long>, PagingAndFilteringCertificateRepository {

}
