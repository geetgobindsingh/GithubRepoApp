package com.geetgobindingh.githubrepoapp.util.rx.scheduler

import io.reactivex.Scheduler

interface SchedulerProvider {

    fun ui(): Scheduler

    fun computation(): Scheduler

    fun io(): Scheduler

}
