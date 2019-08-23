package com.example.batch.job.config;

import com.example.batch.entity.DummyEntity;
import com.example.batch.entity.MemberEntity;
import com.example.batch.job.JobExecutor;
import com.example.batch.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        cronTriggerFactoryBean.setCronExpression("0 0/1 * * * ?");
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
//                .listener(new JobListener())
                .start(memberDeleteStep())
                .build();
    }

    @Bean
    @JobScope
    public Step memberDeleteStep() {
        return stepBuilderFactory
                .get("memberDeleteStep")
                .<MemberEntity, DummyEntity>chunk(1000)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<MemberEntity> reader() {
        RepositoryItemReader<MemberEntity> reader = new RepositoryItemReader<>();
        reader.setRepository(memberRepository);
        reader.setMethodName("findAllByLastSignInDateTimeBeforeAndDeletedIsFalse");
        reader.setArguments(Collections.singletonList(LocalDateTime.of(2019, 6, 10, 0, 0, 0)));
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);
        reader.setSort(sorts);
        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<MemberEntity, MemberEntity> processor1() {
        return item -> {
            System.out.println("### " + item.getDummyEntity());
            item.setDeleted(true);
            log.info("Member #{} is deleted.", item.getId());
            return item;
        };
    }

    @Bean
    @StepScope
    public ItemProcessor<MemberEntity, DummyEntity> processor2() {
        return item -> {
            if (item.getDummyEntity() != null) {
                DummyEntity dummyEntity = item.getDummyEntity();
                dummyEntity.setText("UPDATED");
                System.out.println("### ALREADY: " + item.getId());
                return dummyEntity;
            } else {
                System.out.println("### NEW: " + item.getId());
                return DummyEntity.builder()
                        .memberEntity(item)
                        .text("NEW")
                        .build();
            }
        };
    }

    @Bean
    @StepScope
    public CompositeItemProcessor<MemberEntity, DummyEntity> processor() {
        CompositeItemProcessor<MemberEntity, DummyEntity> processor = new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(processor1(), processor2()));
        return processor;
    }

    @Bean
    @StepScope
    public ItemWriter<DummyEntity> writer() {
        JpaItemWriter<DummyEntity> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}
