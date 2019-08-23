package com.example.batch.component;

import com.example.batch.entity.DummyEntity;
import com.example.batch.entity.MemberEntity;
import com.example.batch.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<MemberEntity> memberEntities = Arrays.asList(
                new MemberEntity(1L, "TEST01", null, LocalDateTime.of(2019, 1, 1, 0, 0, 0), false),
                new MemberEntity(2L, "TEST02", null, LocalDateTime.of(2019, 2, 1, 0, 0, 0), false),
                new MemberEntity(3L, "TEST03", null, LocalDateTime.of(2019, 3, 1, 0, 0, 0), false),
                new MemberEntity(4L, "TEST04", null, LocalDateTime.of(2019, 4, 1, 0, 0, 0), false),
                new MemberEntity(5L, "TEST05", null, LocalDateTime.of(2019, 5, 1, 0, 0, 0), false),
                new MemberEntity(6L, "TEST06", null, LocalDateTime.of(2019, 6, 1, 0, 0, 0), false),
                new MemberEntity(7L, "TEST07", null, LocalDateTime.of(2019, 7, 1, 0, 0, 0), false),
                new MemberEntity(8L, "TEST08", null, LocalDateTime.of(2019, 1, 2, 0, 0, 0), false),
                new MemberEntity(9L, "TEST09", null, LocalDateTime.of(2019, 2, 2, 0, 0, 0), false),
                new MemberEntity(10L, "TEST10", null, LocalDateTime.of(2019, 3, 2, 0, 0, 0), false),
                new MemberEntity(11L, "TEST11", null, LocalDateTime.of(2019, 4, 2, 0, 0, 0), false),
                new MemberEntity(12L, "TEST12", null, LocalDateTime.of(2019, 5, 2, 0, 0, 0), false),
                new MemberEntity(13L, "TEST13", null, LocalDateTime.of(2019, 6, 2, 0, 0, 0), false),
                new MemberEntity(14L, "TEST14", null, LocalDateTime.of(2019, 7, 2, 0, 0, 0), false),
                new MemberEntity(15L, "TEST15", null, LocalDateTime.of(2019, 1, 3, 0, 0, 0), false),
                new MemberEntity(16L, "TEST16", null, LocalDateTime.of(2019, 2, 3, 0, 0, 0), false),
                new MemberEntity(17L, "TEST17", null, LocalDateTime.of(2019, 3, 3, 0, 0, 0), false),
                new MemberEntity(18L, "TEST18", null, LocalDateTime.of(2019, 4, 3, 0, 0, 0), false),
                new MemberEntity(19L, "TEST19", null, LocalDateTime.of(2019, 5, 3, 0, 0, 0), false),
                new MemberEntity(20L, "TEST20", null, LocalDateTime.of(2019, 6, 3, 0, 0, 0), false)
        );

        memberEntities.get(3).setDummyEntity(new DummyEntity(null, memberEntities.get(3), "ABCDE", null, null));
        memberEntities.get(4).setDummyEntity(new DummyEntity(null, memberEntities.get(4), "ABCDE", null, null));
        memberEntities.get(5).setDummyEntity(new DummyEntity(null, memberEntities.get(5), "ABCDE", null, null));
        memberEntities.get(6).setDummyEntity(new DummyEntity(null, memberEntities.get(6), "ABCDE", null, null));
        memberEntities.get(7).setDummyEntity(new DummyEntity(null, memberEntities.get(7), "ABCDE", null, null));

        memberRepository.saveAll(memberEntities);
    }

}
