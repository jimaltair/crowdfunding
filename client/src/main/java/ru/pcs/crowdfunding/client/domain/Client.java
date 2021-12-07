package ru.pcs.crowdfunding.client.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"projects"})
@EqualsAndHashCode(exclude = {"projects"})
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

//    @Column(name = "client_image", nullable = false)
////    @OneToOne(mappedBy = "client")
    private ClientImage image;

    @Column(name = "account_id", unique = true)
    private Long accountId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    private List<Project> projects;
}
