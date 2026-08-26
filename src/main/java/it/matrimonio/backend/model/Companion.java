package it.matrimonio.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "companions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Companion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private String allergies;

    @Enumerated(EnumType.STRING)
    private MenuType menuType;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private Guest guest;
}