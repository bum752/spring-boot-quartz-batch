package com.example.batch.job.config;

import com.example.batch.entity.MemberEntity;
import com.example.batch.job.JobExecutor;
import com.example.batch.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MemberDeleteBatchJobConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final MemberRepository memberRepository;

    @Bean
    public CronTriggerFactoryBean memberDeleteJobCronTrigger() {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setCronExpression("0/30 * * * * ?");
        cronTriggerFactoryBean.setJobDetail(memberDeleteJobDetail().getObject());
        return cronTriggerFactoryBean;
    }

    @Bean
    public JobDetailFactoryBean memberDeleteJobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        Map<String, Object> jobDataAsMap = new HashMap<>();
        jobDataAsMap.put("job", memberDeleteJob().getName());
        jobDetailFactoryBean.setJobDataAsMap(jobDataAsMap);
        jobDetailFactoryBean.setJobClass(JobExecutor.class);
        return jobDetailFactoryBean;
    }

    @Bean
    public Job memberDeleteJob() {
        return jobBuilderFactory
                .get("memberDeleteJob")
                .start(memberDeleteStep())
                .build();
    }

    @Bean
    public Step memberDeleteStep() {
        return stepBuilderFactory
                .get("memberDeleteStep")
                .<MemberEntity, MemberEntity>chunk(1000)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    private ItemReader<MemberEntity> reader() {
        RepositoryItemReader<MemberEntity> reader = new RepositoryItemReader<>();
        reader.setRepository(memberRepository);
        reader.setMethodName("findAllByLastSignInDateTImeBefore");
        reader.setArguments(Collections.singletonList(LocalDateTime.of(2019, 6, 10, 0, 0, 0)));
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        reader.setSort(sorts);
        return reader;
    }

    private ItemProcessor<MemberEntity, MemberEntity> processor() {
        return item -> {
            item.setDeleted(true);
            log.info("Member #{} is deleted.", item.getId());
            return item;
        };
    }

    private ItemWriter<MemberEntity> writer() {
        JpaItemWriter<MemberEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}
