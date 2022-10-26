package com.saeinlee.kotlinspringbatch.reader

import com.saeinlee.kotlinspringbatch.user.User
import com.saeinlee.kotlinspringbatch.user.Repositories
import com.saeinlee.kotlinspringbatch.user.UserStatus
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
@StepScope
class InactiveUserReader: ItemReader<User> {

    @Value("#{jobParameters[requestDate]}")
    private lateinit var requestDate: String

    @Autowired
    private lateinit var userRepositories: Repositories
    private lateinit var list: MutableList<User>
    private var nextIdx: Int = 0

    @PostConstruct
    fun postConstruct() {
    }

    override fun read(): User? {
        list = userRepositories.findByUserStatusIsAndUpdateDateBefore(UserStatus.ACTIVE, requestDate)

        if(nextIdx < list.size) {
            return list[nextIdx++]
        }
        return null
    }


}