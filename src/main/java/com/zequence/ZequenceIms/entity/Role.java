package com.zequence.ZequenceIms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_title", length = 255)
    private ERole roleTitle;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns=@JoinColumn(name="USER_ID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="ROLE_ID", referencedColumnName="ID"))
    private List<User> users = new ArrayList<>();
}
