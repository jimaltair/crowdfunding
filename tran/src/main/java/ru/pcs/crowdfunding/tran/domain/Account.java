package ru.pcs.crowdfunding.tran.domain;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"payments"})
@EqualsAndHashCode(exclude = {"payments"})
@Entity
@Table(name = "account")
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
