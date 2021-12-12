package ru.pcs.crowdfunding.tran.domain;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account")
@ToString(exclude = {"payments"})
@EqualsAndHashCode(exclude = {"payments"})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "date_created", nullable = false)
    private Instant createdAt;

    @Column(name = "date_modified", nullable = false)
    private Instant modifiedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<Payment> payments;

}