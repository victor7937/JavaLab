package com.epam.esm.dto;

public class OrderDTO {

    private String usersEmail;
    private Long certificateId;

    public OrderDTO() {
    }

    public OrderDTO(String usersEmail, Long certificateId) {
        this.usersEmail = usersEmail;
        this.certificateId = certificateId;
    }

    public String getUsersEmail() {
        return usersEmail;
    }

    public void setUsersEmail(String usersEmail) {
        this.usersEmail = usersEmail;
    }

    public Long getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Long certificateId) {
        this.certificateId = certificateId;
    }
}
