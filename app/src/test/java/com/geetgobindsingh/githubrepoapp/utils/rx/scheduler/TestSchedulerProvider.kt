package com.geetgobindingh.githubrepoapp.utils.rx.scheduler

import com.geetgobindingh.githubrepoapp.util.rx.scheduler.SchedulerProvider
import io.reactivex.Scheduler

class TestSchedulerProvider(private val scheduler: Scheduler) :
    SchedulerProvider {

    override fun ui(): Scheduler {
        return scheduler
    }

    override fun computation(): Scheduler {
        return scheduler
    }

    override fun io(): Scheduler {
        return scheduler
    }

}
