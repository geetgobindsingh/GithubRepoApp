package com.geetgobindingh.githubrepoapp.data.mapper.base

interface Mapper<E, D> {
    fun map(it: E): D
}