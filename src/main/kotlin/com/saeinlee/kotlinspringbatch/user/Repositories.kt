package com.saeinlee.kotlinspringbatch.user;

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.time.LocalDateTime

interface Repositories : JpaRepository<User, Long> {
    fun findByUserStatusIsAndUpdateDateBefore(userStatus: UserStatus, updateDate: String): MutableList<User>
}