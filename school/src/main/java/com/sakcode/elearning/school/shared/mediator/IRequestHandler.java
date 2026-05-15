package com.sakcode.elearning.school.shared.mediator;

public interface IRequestHandler<TRequest extends IRequest<TResponse>, TResponse> {
  TResponse handle(TRequest request);
}
