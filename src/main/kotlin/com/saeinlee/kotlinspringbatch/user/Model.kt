package com.saeinlee.kotlinspringbatch.user

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

enum class UserStatus{
    INACTIVE, ACTIVE
}

enum class Authority {
    ROLE_USER, ROLE_ADMIN
}

@Entity
@Table(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null,
    var name: String? = null,
    var email: String? = null,
    var password: String? = null,
    var userStatus: UserStatus? = UserStatus.INACTIVE,
    @Enumerated(EnumType.STRING)
    var authority: Authority?= Authority.ROLE_USER,
    var createDate: LocalDateTime?= null,
    var updateDate: String?= null
): Serializable