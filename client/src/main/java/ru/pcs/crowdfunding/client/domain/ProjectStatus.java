package ru.pcs.crowdfunding.client.domain;

import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString()
@EqualsAndHashCode()
@Entity
@Table(name = "project_status")
public class ProjectStatus {
    public enum Status {
        NOT_CONFIRMED,
        CONFIRMED,
        FINISHED,
        CANCELED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
