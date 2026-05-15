package com.sakcode.elearning.school.shared.mediator;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Mediator {

  private final ApplicationContext applicationContext;

  public Mediator(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @SuppressWarnings("unchecked")
  public <TRequest extends IRequest<TResponse>, TResponse> TResponse send(TRequest request) {
    Class<?> handlerClass = resolveHandlerClass(request);
    IRequestHandler<TRequest, TResponse> handler =
        (IRequestHandler<TRequest, TResponse>) applicationContext.getBean(handlerClass);
    return handler.handle(request);
  }

  @SuppressWarnings("unchecked")
  private <TRequest extends IRequest<TResponse>, TResponse> Class<?> resolveHandlerClass(
      TRequest request) {
    String requestClassName = request.getClass().getName();
    String handlerClassName = requestClassName.replace("Request", "Handler");
    try {
      return Class.forName(handlerClassName);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Handler not found for request: " + requestClassName, e);
    }
  }
}
