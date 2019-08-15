package com.example.batch.config;

import com.example.batch.component.SchedulerJobFactory;
import lombok.RequiredArgsConstructor;
import org.quartz.Trigger;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties(value = {QuartzProperties.class})
@EnableBatchProcessing
@RequiredArgsConstructor
public class QuartzBatchConfig {

    private final DataSource dataSource;
    private final JobRegistry jobRegistry;
    private final QuartzProperties quartzProperties;
    private final SchedulerJobFactory schedulerJobFactory;

    // https://stackoverflow.com/a/28937617
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(Trigger[] triggers) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        Properties properties = new Properties();
        properties.putAll(quartzProperties.getProperties());

        schedulerFactoryBean.setSchedulerName("memberDeleteJobScheduler");
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setJobFactory(schedulerJobFactory);
        schedulerFactoryBean.setQuartzProperties(properties);
        schedulerFactoryBean.setTriggers(triggers);

        return schedulerFactoryBean;
    }

}
