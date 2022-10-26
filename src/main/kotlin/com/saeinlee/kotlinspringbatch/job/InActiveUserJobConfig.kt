package com.saeinlee.kotlinspringbatch.job

import com.saeinlee.kotlinspringbatch.user.User
import com.saeinlee.kotlinspringbatch.reader.InactiveUserReader
import com.saeinlee.kotlinspringbatch.user.Repositories
import com.saeinlee.kotlinspringbatch.user.UserStatus
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.ZoneId

@Configuration
class InActiveUserJobConfig(
    val jobBuilderFactory: JobBuilderFactory,
    val stepBuilderFactory: StepBuilderFactory,
    val userRepository: Repositories
) {
    private val logger = KotlinLogging.logger {}
    private final val now = LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString()
    private final val INACTIVE_USER_JOB = "INACTIVE_USER_JOB_" + now
    private final val INACTIVE_USER_JOB_STEP = INACTIVE_USER_JOB + "_STEP"
    private final val CHUNK_SIZE = 10

    @Bean
    fun Job(): Job {
        return jobBuilderFactory.get(INACTIVE_USER_JOB)
            .start(step(now))
            .build()
    }

    @Bean
    fun step(@Value("#{jobParameters[requestDate]}") requestDate: String?): Step {
        return stepBuilderFactory.get(INACTIVE_USER_JOB_STEP)
            .chunk<User, User>(CHUNK_SIZE)
            .reader(reader(requestDate))
            .processor(processor())
            .writer(writer())
            .build()
    }

    @Bean
    @StepScope
    fun reader (@Value("#{jobParameters[requestDate]}") requestDate: String?): InactiveUserReader {
        return InactiveUserReader("")
    }

    @Bean
    fun processor(): ItemProcessor<User, User> {
        return ItemProcessor {
            it.userStatus = UserStatus.INACTIVE
            it
        }
    }

    @Bean
    fun writer(): ItemWriter<User> {
        return ItemWriter<User> {
            userRepository.saveAll(it)
        }
    }
}