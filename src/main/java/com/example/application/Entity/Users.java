    package com.example.application.Entity;

    import com.example.application.Enums.Roles;
    import jakarta.persistence.*;
    import lombok.Data;
    import lombok.Getter;
    import lombok.Setter;

    @Data
    @Getter
    @Setter
    @Entity
    public class Users {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer userId;
        @Column(nullable = false)
        private String userName;
        @Column(nullable = false)
        private String email;
        @Column(nullable = false)
        private Long phno;
        @Column(nullable = false)
        private String password;
        @Column(nullable = false)
        private Roles userRole;

    }
