package org.star.easyflow.core.models.contract;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.easyflow.core.models.assigment.Assigment;
import org.star.easyflow.core.models.user.User;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "contract", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"assigmenter_id", "holder_id"})
})
@Getter
@Setter
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "assigmenter_id", referencedColumnName = "id", nullable = false)
    private User assigmenter;

    @ManyToOne
    @JoinColumn(name = "holder_id", referencedColumnName = "id", nullable = false)
    private User holder;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.REMOVE)
    private List<Assigment> assigments;
}

