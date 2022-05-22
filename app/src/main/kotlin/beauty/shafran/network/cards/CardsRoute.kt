package beauty.shafran.network.cards

import beauty.shafran.network.cards.executor.CardsExecutor
import beauty.shafran.network.utils.callWrapper
import io.ktor.server.routing.*

fun Route.cardsRoute(executor: CardsExecutor) {
    get("/getCardById", callWrapper(executor::getCardById))
    get("/getCardByCustomerId", callWrapper(executor::getCardByCustomerId))
    post("/createCard", callWrapper(executor::createCards))
}