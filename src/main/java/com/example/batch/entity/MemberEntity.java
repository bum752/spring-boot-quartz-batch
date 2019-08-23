package com.example.batch.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "member")
@Table(name = "member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(
        name = "MemberEntity.withDummyEntity",
        attributeNodes = {@NamedAttributeNode(value = "dummyEntity")}
)
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @Column(name = "member_name")
    private String name;

    @OneToOne(mappedBy = "memberEntity", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private DummyEntity dummyEntity;

    @CreatedDate
    @Column(name = "last_sign_in", updatable = false)
    private LocalDateTime lastSignInDateTime;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean deleted = false;

}
