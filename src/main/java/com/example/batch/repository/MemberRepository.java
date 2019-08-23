package com.example.batch.repository;

import com.example.batch.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    @EntityGraph(value = "MemberEntity.withDummyEntity")
    Page<MemberEntity> findAllByLastSignInDateTimeBeforeAndDeletedIsFalse(LocalDateTime standardDateTime, Pageable pageable);

}
