package us.surve.routing

import io.ktor.application.ApplicationCall
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

inline fun getRoute(
    instance: Routing,
    path: String,
    crossinline action: (PipelineContext<Unit, ApplicationCall>) -> Unit
) {
    instance.get(path) {
        action(this)
    }
}