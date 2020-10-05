package com.template.stream;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
public class StreamController {

    @GetMapping("/stream")
    public StreamingResponseBody handleRequest() {
        return new StreamingResponseBody() {

            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                for(int i = 0; i < 1000; i++) {
                    outputStream.write((Integer.toString(i) + " - ")
                            .getBytes());
                    outputStream.flush();
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
