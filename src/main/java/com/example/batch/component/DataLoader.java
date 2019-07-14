package com.example.batch.component;

import com.example.batch.entity.MemberEntity;
import com.example.batch.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Collection<MemberEntity> memberEntities = Arrays.asList(
                new MemberEntity(null, "TEST01", LocalDateTime.of(2019, 1, 1, 0, 0, 0), false),
                new MemberEntity(null, "TEST02", LocalDateTime.of(2019, 2, 1, 0, 0, 0), false),
                new MemberEntity(null, "TEST03", LocalDateTime.of(2019, 3, 1, 0, 0, 0), false),
                new MemberEntity(null, "TEST04", LocalDateTime.of(2019, 4, 1, 0, 0, 0), false),
                new MemberEntity(null, "TEST05", LocalDateTime.of(2019, 5, 1, 0, 0, 0), false),
                new MemberEntity(null, "TEST06", LocalDateTime.of(2019, 6, 1, 0, 0, 0), false),
                new MemberEntity(null, "TEST07", LocalDateTime.of(2019, 7, 1, 0, 0, 0), false),
                new MemberEntity(null, "TEST08", LocalDateTime.of(2019, 1, 2, 0, 0, 0), false),
                new MemberEntity(null, "TEST09", LocalDateTime.of(2019, 2, 2, 0, 0, 0), false),
                new MemberEntity(null, "TEST10", LocalDateTime.of(2019, 3, 2, 0, 0, 0), false),
                new MemberEntity(null, "TEST11", LocalDateTime.of(2019, 4, 2, 0, 0, 0), false),
                new MemberEntity(null, "TEST12", LocalDateTime.of(2019, 5, 2, 0, 0, 0), false),
                new MemberEntity(null, "TEST13", LocalDateTime.of(2019, 6, 2, 0, 0, 0), false),
                new MemberEntity(null, "TEST14", LocalDateTime.of(2019, 7, 2, 0, 0, 0), false),
                new MemberEntity(null, "TEST15", LocalDateTime.of(2019, 1, 3, 0, 0, 0), false),
                new MemberEntity(null, "TEST16", LocalDateTime.of(2019, 2, 3, 0, 0, 0), false),
                new MemberEntity(null, "TEST17", LocalDateTime.of(2019, 3, 3, 0, 0, 0), false),
                new MemberEntity(null, "TEST18", LocalDateTime.of(2019, 4, 3, 0, 0, 0), false),
                new MemberEntity(null, "TEST19", LocalDateTime.of(2019, 5, 3, 0, 0, 0), false),
                new MemberEntity(null, "TEST20", LocalDateTime.of(2019, 6, 3, 0, 0, 0), false)
        );
        memberRepository.saveAll(memberEntities);
    }

}
