package ru.pcs.crowdfunding.client.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString()
@EqualsAndHashCode()
@Entity
public class ClientImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] content;

    private String name;

//    @OneToOne(mappedBy = "i/", fetch = FetchType.LAZY)
//    @JoinColumn(name = "client_id", nullable = false)
    @MapsId
    @JoinColumn(name = "id")
    private Client client;

}
