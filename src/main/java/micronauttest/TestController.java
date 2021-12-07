package micronauttest;

import io.micronaut.context.annotation.Context;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

@Controller
@Context
public class TestController {

  @Post(uris = {"/test"})
  public HttpResponse<?> postJsonRpc(
      @Nullable String param, @Body String body) {
    return HttpResponse.ok("ok");
  }
}
