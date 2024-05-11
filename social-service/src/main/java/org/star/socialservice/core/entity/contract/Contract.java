package org.star.socialservice.core.entity.contract;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.socialservice.core.entity.assigment.SocialAssigment;
import org.star.socialservice.core.entity.user.SocialUser;

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
    private SocialUser assigmenter;

    @ManyToOne
    @JoinColumn(name = "holder_id", referencedColumnName = "id", nullable = false)
    private SocialUser holder;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.REMOVE)
    private List<SocialAssigment> assigments;
}

