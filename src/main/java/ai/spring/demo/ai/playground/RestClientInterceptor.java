package ai.spring.demo.ai.playground;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RestClientInterceptor implements ClientHttpRequestInterceptor {

    static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {
        ClientHttpResponse response;
        byte[] body;

        BufferingClientHttpResponseWrapper(ClientHttpResponse response) {
            this.response = response;
        }

        @Override
        public HttpStatusCode getStatusCode() throws IOException {
            return response.getStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return response.getStatusText();
        }

        @Override
        public void close() {
            response.close();
        }

        @Override
        public HttpHeaders getHeaders() {
            return response.getHeaders();
        }

        @Override
        public InputStream getBody() throws IOException {
            if (this.body == null) {
                this.body = StreamUtils.copyToByteArray(this.response.getBody());
            }
            return new ByteArrayInputStream(this.body);
        }
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // log the request
        logRequest(request, body);

        // execute the request
        ClientHttpResponse response = execution.execute(request, body);

        // wrap the response that buffers the body, so that we can read from it multiple times
        BufferingClientHttpResponseWrapper responseWrapper = new BufferingClientHttpResponseWrapper(response);

        // log the response
        logResponse(responseWrapper);

        // return the wrapped response
        return responseWrapper;
    }

    static void logRequest (HttpRequest request, byte[] bytes) {
        System.out.printf("Request: %s %s%n", request.getMethod(), request.getURI());
        System.out.printf("Headers: %s%n", request.getHeaders());
        System.out.printf("Body: %s%n", new String(bytes));
        System.out.println();
    }

    static void logResponse (ClientHttpResponse response) throws IOException {
        System.out.printf("Response: %s %s%n", response.getStatusCode(), response.getStatusText());
        System.out.printf("Headers: %s%n", response.getHeaders());
        System.out.printf("Body: %s%n", IOUtils.toString(response.getBody(), StandardCharsets.UTF_8).length());
        System.out.println();
    }
}
