package hello.springmvc.basic.request02;
/*
    HTTP message body에 데이터를 직접 담아서 요청
     a. HTTP API에서 주로 사용 = JSON, XML, TEXT
     b. 데이터 형식은 주로 JSON 사용
     c. POST, PUT, PATCH
    [참고]
    요청 파라미터와 다르게, HTTP 메시지 바디를 통해 데이터가 직접 넘어오는 경우 =
    @RequestParam , @ModelAttribute 를 사용할 수 없음.
    (물론 HTML Form 형식으로 전달되는 경우는 요청 파라미터로 인정됨.)
*/
/* #1. 단순한 텍스트 메시지를 HTTP 메시지 바디에 담아서 전송하고, 읽기.
       HTTP 메시지 바디의 데이터를 InputStream 을 사용해서 직접 읽을 수 있음.*/
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringControler03 {
    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        // request객체에 InputStream 얻기
        ServletInputStream inputStream = request.getInputStream();
        // 바이트 코드인 InputStream을 문자열과 인코딩방식으로 만들어줌
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        response.getWriter().write("ok");
    }

/*
    #2. Input, Output 스트림, Reader

    @ 스프링 MVC는 다음 파라미터를 지원.
     a. InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
     b. OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
*/
    @PostMapping("/request-body-string-v2")
    public void requestBodyString(InputStream inputStream, Writer responseWriter) throws IOException {
        // 바이트 코드인 InputStream을 문자열과 인코딩방식으로 만들어줌
        // OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }

/*
    #3. HttpEntity
     : HTTP header, body 정보를 편리하게 조회
       = 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
       = HttpMessageConverter 사용 -> StringHttpMessageConverter 적용

    응답에서도 HttpEntity 사용 가능
     = 메시지 바디 정보 직접 반환(view 조회X)
     = HttpMessageConverter 사용 -> StringHttpMessageConverter 적용

     @ 스프링 MVC는 다음 파라미터를 지원.
       a. HttpEntity: HTTP header, body 정보를 편리하게 조회
         - 메시지 바디 정보를 직접 조회
         - 요청 파라미터를 조회하는 기능과 관계 없음 = 쿼리스트링과 폼데이터를 말함 @RequestParam X, @ModelAttribute X
       b. HttpEntity는 응답에도 사용 가능 = return에서 new 생성해서 사용
         - 메시지 바디 정보 직접 반환 헤더 정보 포함 가능
         - view 조회X  = 바로 http body에 담아서 리턴해주지 view단하고는 상관없음

     @ HttpEntity 를 상속받은 다음 객체들도 같은 기능을 제공 => 하지만, 참고만 할것
       a. RequestEntity
         - HttpMethod, url 정보가 추가, 요청에서 사용
       b. ResponseEntity
         - HTTP 상태 코드 설정 가능, 응답에서 사용
          (예시코드)
           return new ResponseEntity<String>("Hello World", responseHeaders,
           HttpStatus.CREATED)
    [참고]
    스프링MVC 내부에서 => HTTP 메시지 바디를 읽어서 문자나 객체로 변환해서 전달해주는데,
    => 이때 HTTP메시지컨버터( HttpMessageConverter )라는 기능을 사용
*/
    // v3부터 실제 SpringMvc에서 제공하는 편리한 기능들 사용 => 이것도 번거로워서 v4에서 애너테이션으로 제공된것 사용
    @PostMapping("/request-body-string-v3")
    // HttpEntity = HTTP body에 있는 것을 <String>으로 변환해서 넣어줄께 => HTTP메시지컨버터가 자동으로 해줌
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) { // 반환타입 HttpEntity 주의해서 보기
        String messageBody = httpEntity.getBody(); // http body에 있는 데이터 가져오기 , <string>이니까 String으로 담기
        log.info("messageBody={}", messageBody); // 로그 출력
        return new HttpEntity<>("ok"); // 리턴하기 | 첫번째 파라미터는 body message임
    }

/*
    #4. @RequestBody - v3 번거로워서 v4에서 애너테이션으로 제공된것 사용 => 실제는 제공된 스프링 자동기능을 많이 사용함
      - 메시지 바디 정보를 직접 조회
        => 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 @RequestParam , @ModelAttribute 와는 전혀 관계가 없음.
      - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
    [참고1]
     헤더 정보가 필요하다면 => HttpEntity 를 사용하거나 @RequestHeader 를 사용하면 됨.
    [참고2]
     @ 요청 파라미터 vs HTTP 메시지 바디
       a. 요청 파라미터를 조회하는 기능: @RequestParam , @ModelAttribute
       b. HTTP 메시지 바디를 직접 조회하는 기능: @RequestBody
    [참고3]
     @ResponseBody
      - 메시지 바디 정보 직접 반환(view를 사용하지 않음)
      - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
*/
    // HTTP 메시지 바디 정보를 편리하게 조회
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {
        log.info("messageBody={}", messageBody);
        return "ok";
    }

/*
    [테스트] Postman을 사용
    POST http://localhost:8282/request-body-string-v???   ??? = 버전 넣어줌
    Body row(문자열 아무거나 넣어줌 hello=>콘솔에 찍힘), Text 선택
*/



} // end of class
