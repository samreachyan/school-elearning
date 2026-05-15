package com.sakcode.elearning.school.shared.mediator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

@ExtendWith(MockitoExtension.class)
class MediatorTest {

  @Mock private ApplicationContext applicationContext;

  @Test
  void shouldSendRequestAndReturnResponse() {
    TestRequest request = new TestRequest();
    TestHandler handler = mock(TestHandler.class);
    TestResponse expectedResponse = new TestResponse("success");
    when(handler.handle(request)).thenReturn(expectedResponse);
    when(applicationContext.getBean(TestHandler.class)).thenReturn(handler);

    Mediator mediator = new Mediator(applicationContext);
    TestResponse response = mediator.send(request);

    assertNotNull(response);
    assertEquals("success", response.getMessage());
    verify(handler).handle(request);
  }

  @Test
  void shouldThrowExceptionWhenHandlerNotFound() {
    Mediator mediator = new Mediator(applicationContext);
    NonExistentRequest request = new NonExistentRequest();

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> mediator.send(request));
    assertTrue(exception.getMessage().contains("Handler not found"));
  }

  // Test classes
  static class TestRequest implements IRequest<TestResponse> {}

  static class TestResponse {
    private final String message;

    TestResponse(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }

  static class TestHandler implements IRequestHandler<TestRequest, TestResponse> {
    @Override
    public TestResponse handle(TestRequest request) {
      return new TestResponse("handled");
    }
  }

  static class NonExistentRequest implements IRequest<String> {}
}
