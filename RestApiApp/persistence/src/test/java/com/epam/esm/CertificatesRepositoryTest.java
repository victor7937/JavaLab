package com.epam.esm;


public class CertificatesRepositoryTest {

//    private static final String DB_CREATE_PATH = "classpath:create_db.sql";
//    private static final String DB_INSERT_PATH = "classpath:insert_data.sql";
//    private static final String ENCODING = "UTF-8";
//    EmbeddedDatabase tempDB;
//
//    GiftCertificateRepository repository;
//
//    @BeforeEach
//    void initDB() {
//        tempDB = new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .setScriptEncoding(ENCODING)
//                .addScript(DB_CREATE_PATH)
//                .addScript(DB_INSERT_PATH)
//                .build();
//        repository = new GiftCertificateRepositoryImpl(new JdbcTemplate(tempDB));
//    }
//
//    @Test
//    void getByTagShouldReturnCorrectCertificates() throws RepositoryException {
//        GiftCertificate giftCertificate = repository.getById(1);
//        Tag firstTag = giftCertificate.getTags().iterator().next();
//        List<GiftCertificate> listByCriteria = repository.getByCriteria(Criteria.createCriteria(Optional.of(firstTag.getName()), Optional.empty(),
//                Optional.empty(), Optional.empty()));
//        listByCriteria.forEach(item -> assertTrue(item.getTags().contains(firstTag)));
//    }
//
//    @Test
//    void getByPartShouldReturnCorrectCertificates() throws RepositoryException {
//        GiftCertificate giftCertificate = repository.getById(1);
//        String name = giftCertificate.getName();
//        String namePart = name.substring(0, name.length() / 2);
//        List<GiftCertificate> listByCriteria = repository.getByCriteria(Criteria.createCriteria(Optional.empty(), Optional.of(name),
//                Optional.empty(), Optional.empty()));
//        listByCriteria.forEach(item -> assertTrue(item.getName().contains(namePart)));
//    }
//
//    @Test
//    void addedCertificateShouldBeReturnedById() throws RepositoryException {
//        GiftCertificate certificateForAdding = new GiftCertificate("name","test tag for adding",23.5f,2);
//        certificateForAdding.addTag(new Tag("test"));
//        certificateForAdding.addTag(new Tag("adding"));
//        GiftCertificate expectedCertificate = repository.add(certificateForAdding);
//        GiftCertificate actualCertificate = repository.getById(expectedCertificate.getId());
//        assertEquals(expectedCertificate, actualCertificate);
//    }
//
//    @Test
//    void DBShouldNotContainsDeletedCertificate () throws RepositoryException {
//        int idForDeleting = 1;
//        GiftCertificate certificateForDeleting = repository.getById(idForDeleting);
//        assertNotNull(certificateForDeleting);
//        assertEquals(idForDeleting, certificateForDeleting.getId());
//        repository.delete(certificateForDeleting.getId());
//        assertThrows(DataNotExistRepositoryException.class, () -> repository.getById(idForDeleting));
//    }
//
//    @Test
//    void DataShouldBeUpdatedCorrectly() throws RepositoryException {
//        GiftCertificate certificateForUpdate = repository.getById(1);
//        Float expectedPrice = certificateForUpdate.getPrice() + 1f;
//        String expectedName = "new " + certificateForUpdate.getName();
//        String expectedDescription = "new " + certificateForUpdate.getDescription();
//        Integer expectedDuration = certificateForUpdate.getDuration() + 1;
//
//        certificateForUpdate.setPrice(expectedPrice);
//        certificateForUpdate.setName(expectedName);
//        certificateForUpdate.setDescription(expectedDescription);
//        certificateForUpdate.setDuration(expectedDuration);
//        certificateForUpdate.addTag(new Tag("new"));
//
//        GiftCertificate certificateModified = repository.update(repository.getById(1), certificateForUpdate);
//        assertEquals(certificateForUpdate, certificateModified);
//    }
//
//    @AfterEach
//    void destroyDB(){
//        tempDB.shutdown();
//    }
}
