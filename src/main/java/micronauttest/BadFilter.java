package micronauttest;

import io.micronaut.context.annotation.Context;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.netty.channel.EventLoopGroup;
import jakarta.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Filter("/**")
@Context
public class BadFilter implements HttpServerFilter {

  @Inject
  public ScheduledExecutorService scheduledExecutorService;
  @Inject
  public EventLoopGroup eventLoopGroup;

  CompletableFuture<Void> delay(int ms) {
    CompletableFuture<Void> future = new CompletableFuture<>();
    scheduledExecutorService.schedule(
        () -> future.complete(null),
        ms,
        TimeUnit.MILLISECONDS
    );
    return future;
  }

  @Override
  public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request,
      ServerFilterChain chain) {
    return Publishers.fromCompletableFuture(
        delay(300).thenComposeAsync(v ->
            Mono.from(chain.proceed(request)).toFuture(), eventLoopGroup));
  }
}
