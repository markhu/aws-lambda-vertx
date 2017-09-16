// VertxHandler.groovy

package com.serverless

import com.amazonaws.services.lambda.runtime.*
import io.vertx.core.*
import java.util.concurrent.*
class VertxHandler implements RequestHandler<Map, Map> {
 // Initialize Vertx instance and deploy UserService Verticle
  final Vertx vertxInstance = {
    System.setProperty('vertx.disableFileCPResolving', 'true')
    final vertx = Vertx.vertx()
    vertx.deployVerticle(UserService.newInstance())
   
    return vertx
  }()
 @Override  Map handleRequest(Map input, Context context) {
    final future = new CompletableFuture<Map>()
   
    // Send message to event bus using httpmethod:resource as dynamic channel    final eventBusAddress = "${input.httpMethod}:${input.resource}"    vertxInstance.eventBus().send(eventBusAddress, input, { asyncResult ->

      if (asyncResult.succeeded()) {
        future.complete(asyncResult.result().body())
     } else {
        future.completeExceptionally(asyncResult.cause())      }
    })
   future.get(5, TimeUnit.SECONDS)
  }
  class UserService extends AbstractVerticle {

    @Override
    void start() throws Exception {

      final eventBus = vertx.eventBus()

      eventBus.consumer('GET:/users') { message ->

        // Do something with Vert.x async, reactive APIs
        message.reply([statusCode: 200, body: 'Received GET:/users'])

      }

      eventBus.consumer('POST:/users') { message ->
        // Do something with Vert.x async, reactive APIs
        message.reply([statusCode: 201, body: 'Received POST:/users'])
      }

      

      eventBus.consumer('GET:/users/{id}') { message ->
        // Do something with Vert.x async, reactive APIs

        message.reply([statusCode: 200, body: 'Received GET:/users/{id}'])

      }



      eventBus.consumer('PUT:/users/{id}') { message ->

        // Do something with Vert.x async, reactive APIs

        message.reply([statusCode: 200, body: 'Received PUT:/users/{id}'])

      }

      

      eventBus.consumer('DELETE:/users/{id}') { message ->

        // Do something with Vert.x async, reactive APIs

        message.reply([statusCode: 200, body: 'Received DELETE:/users/{id}'])

      }

    }

  }

}
