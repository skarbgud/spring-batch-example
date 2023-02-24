# spring-batch-example
스프링 배치 메타 테이블 엔티티 관계도
![Untitled](https://user-images.githubusercontent.com/54926902/221280220-894293de-0c15-490e-98db-a7fa5fd056e5.png)



****BATCH_JOB_INSTANCE****

- `JOB_INSTANCE_ID`
    - `BATCH_JOB_INSTANCE` 테이블의 PK
- `JOB_NAME`
    - 수행한 Batch Job Name

**BATCH_JOB_EXECUTION**

- **JOB_EXECUTION와 JOB_INSTANCE는 부모-자식 관계**
    
    JOB_EXECUTION은 자신의 부모 **JOB_INSTACNE가 성공/실패했던 모든 내역을 가지고 있음**
    ```
    Job ⇒ simpleJob
    
    Job Instance ⇒ Job Parameter를 20230225로 실행한 simpleJob(JobParameter 단위로 생성)
    
    Job Execution ⇒ Job Parameter를 20230225로 실행한 simpleJob의 1번째 시도 혹은 다음번 시도
    ```
