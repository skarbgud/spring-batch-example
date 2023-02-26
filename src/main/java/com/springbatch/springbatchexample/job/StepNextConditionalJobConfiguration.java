package com.springbatch.springbatchexample.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StepNextConditionalJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get("stepNextConditionalJob")
                .start(conditionalJobStep1())
                .on("FAILED")   //FAIL일 경우
                .to(conditionalJobStep3()) //step3로 간다
                .on("*") //step3의 결과 관계없이
                .end() //step3로 이동하면 Flow 종료
                .from(conditionalJobStep1()) //step1로 부터
                .on("*") //FAIL 외의 모든경우
                .to(conditionalJobStep2()) //step2로 이동
                .next(conditionalJobStep3()) //step2가 정상 종료되면 step3로 이동
                .on("*") //step3의 결과 관계없이
                .end() // step3로 이동하면 flow 종료
                .end() //job 종료
                .build();
    }

    /**
     * 코드 시나리오
     *
     * step1 실패 시나리오 : step1 -> step3
     * step2 성공 시나리오 : step1 -> step2 -> step3
     */

    @Bean
    public Step conditionalJobStep1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>> This is stepNextConditionalJob Step1");

                    /**
                     * ExitStatus를 Fail로 지정한다
                     * 해당 status를 보고 flow가 진행된다.
                     */
//                    contribution.setExitStatus(ExitStatus.FAILED);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step conditionalJobStep2() {
        return stepBuilderFactory.get("conditionalJobStep2")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>> This is stepNextConditionalJob Step2");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

    @Bean
    public Step conditionalJobStep3() {
        return stepBuilderFactory.get("conditionalJobStep3")
                .tasklet(((contribution, chunkContext) -> {
                    log.info(">>> This is stepNextConditionalJob Step3");
                    return RepeatStatus.FINISHED;
                }))
                .build();
    }

}
