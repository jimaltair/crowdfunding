package ru.pcs.crowdfunding.tran.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "date_created", nullable = false)
    private Instant createdAt;

    @Column(name = "date_modified", nullable = false)
    private Instant modifiedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<Payment> payments;
}
