package ru.pcs.crowdfunding.client.domain;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString()
@EqualsAndHashCode()
@Entity
@Table(name = "project_comment")
public class ProjectComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", nullable = false)
    private User author;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "text", nullable = false)
    @Lob
    private String text;
}
