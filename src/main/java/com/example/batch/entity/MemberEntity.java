package com.example.batch.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "member")
@Table(name = "member")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", updatable = false)
    private Long id;

    @Column(name = "member_name")
    private String name;

    @CreatedDate
    @Column(name = "last_sign_in", updatable = false)
    private LocalDateTime lastSignInDateTIme;

    @Builder.Default
    @Column(name = "is_deleted")
    private Boolean deleted = false;

}
