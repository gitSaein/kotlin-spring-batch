package com.saeinlee.kotlinspringbatch.domain

import javax.persistence.*

enum class UserStatus {
    ACTIVE, INACTIVE
}

@Entity
@Table(name = "user")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Long? = null

}