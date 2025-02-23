package app.mathias.sector.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sectors")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectorEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50, updatable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", updatable = false)
    private SectorEntity parent;

    @OrderBy("name")
    @OneToMany(mappedBy = "parent", cascade = CascadeType.REFRESH)
    private List<SectorEntity> children;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", updatable = false)
    private LocalDateTime updatedAt;

}