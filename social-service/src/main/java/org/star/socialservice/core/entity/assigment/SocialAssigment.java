package org.star.socialservice.core.entity.assigment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.socialservice.core.entity.contract.Contract;

@Entity
@Table(name = "social_assigment", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"contract_id", "task_id"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SocialAssigment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;
}
