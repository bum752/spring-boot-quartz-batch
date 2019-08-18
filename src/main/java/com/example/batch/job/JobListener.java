package com.example.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobInstance;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class JobListener implements JobExecutionListener {

    private static final String INCOMING_WEBHOOK_URL = System.getenv("INCOMING_WEBHOOK_URL");
    private static final StringHttpMessageConverter utf8Converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
    private static final RestTemplate restTemplate = new RestTemplate();

    public JobListener() {
        restTemplate
                .getMessageConverters()
                .add(0, utf8Converter);
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        JobInstance jobInstance = jobExecution.getJobInstance();
        long jobInstanceId = jobInstance.getId();
        String jobName = jobInstance.getJobName();
        BatchStatus batchStatus = jobExecution.getStatus();

        String color = this.getColorByBatchStatus(batchStatus);
        String title = String.format(
                "#%d `%s` (%s)",
                jobInstanceId,
                jobName,
                this.getExecutionTimeStringByJobExecution(jobExecution)
        );
        String value = batchStatus.toString();
        if (!BatchStatus.COMPLETED.equals(batchStatus)) {
            value += "\n";
            value += jobExecution.getAllFailureExceptions().toString();
        }
        boolean shortFormat = false;

        JSONObject field = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            field.put("title", title);
            field.put("value", value);
            field.put("short", shortFormat);
            List<JSONObject> fields = Collections.singletonList(field);
            jsonObject.put("pretext", jobName + " is done.");
            jsonObject.put("color", color);
            jsonObject.put("fields", fields);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(jsonObject.toString());
        this.sendSlackNotification(httpEntity);
    }

    private String getColorByBatchStatus(BatchStatus batchStatus) {
        if (BatchStatus.COMPLETED.equals(batchStatus))
            return "good";
        if (BatchStatus.FAILED.equals(batchStatus))
            return "danger";
        return "warning";
    }

    private String getExecutionTimeStringByJobExecution(JobExecution jobExecution) {
        long executionTime = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
        long hours = TimeUnit.MILLISECONDS.toHours(executionTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(executionTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(executionTime);
        long milliSeconds = TimeUnit.MILLISECONDS.toMillis(executionTime);
        return String.format("%d시간 %d분 %d초 %d", hours, minutes, seconds, milliSeconds);
    }

    private void sendSlackNotification(HttpEntity httpEntity) {
        ResponseEntity<String> responseEntity = restTemplate
                .exchange(INCOMING_WEBHOOK_URL, HttpMethod.POST, httpEntity, String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("슬랙 발송 실패");
        }
    }

}
