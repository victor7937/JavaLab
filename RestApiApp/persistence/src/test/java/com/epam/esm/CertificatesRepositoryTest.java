package com.epam.esm;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.exception.DataNotExistRepositoryException;
import com.epam.esm.exception.RepositoryException;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.repository.impl.GiftCertificateRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import com.epam.esm.entity.Tag;


public class CertificatesRepositoryTest {

    private static final String DB_CREATE_PATH = "classpath:create_db.sql";
    private static final String DB_INSERT_PATH = "classpath:insert_data.sql";
    private static final String ENCODING = "UTF-8";
    EmbeddedDatabase tempDB;

    GiftCertificateRepository repository;

    @BeforeEach
    void initDB() {
        tempDB = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setScriptEncoding(ENCODING)
                .addScript(DB_CREATE_PATH)
                .addScript(DB_INSERT_PATH)
                .build();
        repository = new GiftCertificateRepositoryImpl(new JdbcTemplate(tempDB));
    }

    @Test
    void addedCertificateShouldBeReturnedById() {
        try {
            GiftCertificate certificateForAdding = new GiftCertificate("name","test tag for adding",23.5f,2);
            certificateForAdding.addTag(new Tag("test"));
            certificateForAdding.addTag(new Tag("adding"));
            GiftCertificate expectedCertificate = repository.add(certificateForAdding);
            GiftCertificate actualCertificate = repository.getById(expectedCertificate.getId());
            assertEquals(expectedCertificate, actualCertificate);
        } catch (RepositoryException e) {
            fail();
        }
    }

    @Test
    void DBShouldNotContainsDeletedCertificate () {
        try {
            int idForDeleting = 1;
            GiftCertificate certificateForDeleting = repository.getById(idForDeleting);
            assertNotNull(certificateForDeleting);
            assertEquals(idForDeleting, certificateForDeleting.getId());

            repository.delete(certificateForDeleting.getId());
            assertThrows(DataNotExistRepositoryException.class, () -> repository.getById(idForDeleting));
        } catch (RepositoryException e) {
            fail();
        }
    }

    @Test
    void DataShouldBeUpdatedCorrectly(){
        try {
            GiftCertificate certificateForUpdate = repository.getById(1);
            Float expectedPrice = certificateForUpdate.getPrice() + 1f;
            String expectedName = "new " + certificateForUpdate.getName();
            String expectedDescription = "new " + certificateForUpdate.getDescription();
            Integer expectedDuration = certificateForUpdate.getDuration() + 1;

            certificateForUpdate.setPrice(expectedPrice);
            certificateForUpdate.setName(expectedName);
            certificateForUpdate.setDescription(expectedDescription);
            certificateForUpdate.setDuration(expectedDuration);
            certificateForUpdate.addTag(new Tag("new"));

            GiftCertificate certificateModified = repository.update(repository.getById(1), certificateForUpdate);
            assertEquals(certificateForUpdate, certificateModified);
        } catch (RepositoryException e) {
            fail();
        }
    }

    @AfterEach
    void destroyDB(){
        tempDB.shutdown();
    }
}
