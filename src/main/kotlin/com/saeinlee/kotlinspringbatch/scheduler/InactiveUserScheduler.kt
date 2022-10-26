package com.saeinlee.kotlinspringbatch.scheduler

import com.saeinlee.kotlinspringbatch.job.InActiveUserJobConfig
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.batch.core.JobParameter
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.ZoneId
import javax.batch.operations.JobExecutionAlreadyCompleteException

@Component
class InactiveUserScheduler(
    val jobLauncher: JobLauncher,
    val inActiveUserJobConfig: InActiveUserJobConfig
) {

    private val logger: Log = LogFactory.getLog(InActiveUserJobConfig::class.java)

    @Scheduled(cron = "0 0 11 1 * *")
    fun runJob() {
        val jobConfig = hashMapOf<String, JobParameter>()
        val now = LocalDate.now(ZoneId.of("Asia/Seoul")).minusMonths(1).minusDays(5)

        jobConfig["requestDate"] = JobParameter(now.toString())
        val jobParameter = JobParameters(jobConfig)
    try{
        jobLauncher.run(inActiveUserJobConfig.Job(), jobParameter)
    } catch(e: JobExecutionAlreadyCompleteException){
        logger.error(e.localizedMessage)
    } catch(e: JobExecutionAlreadyRunningException){
        logger.error(e.localizedMessage)
    } catch(e: JobParametersInvalidException){
        logger.error(e.localizedMessage)
    }

    }
}