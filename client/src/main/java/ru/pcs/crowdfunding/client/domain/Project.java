package ru.pcs.crowdfunding.client.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"images", "comments", "status"})
@EqualsAndHashCode(exclude = {"images", "comments", "status"})
@Entity
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Client author;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 4096)
    private String description;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "finish_date", nullable = false)
    private Instant finishDate;

    @Column(name = "money_goal", nullable = false)
    private BigDecimal moneyGoal;

    @Column(name = "account_id", nullable = false, unique = true)
    private Long accountId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private List<ProjectImage> images;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    private List<ProjectComment> comments;

    @OneToOne(mappedBy = "project")
    private ProjectStatus status;
}
