package hello.springmvc.basic.request02;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
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
import java.nio.charset.StandardCharsets;

/*
    #. HTTP 요청 메시지 - JSON
    [JSON 형식 요청 메세지 형식]
    {"username":"hello", "age":20}
    content-type: application/json
*/
@Slf4j
@Controller
public class RequestBodyJsonController04 {
    // 문자로 된 JSON 데이터를 Jackson 라이브러리인 objectMapper 를 사용해서 자바 객체로 변환
    private ObjectMapper objectMapper = new ObjectMapper(); //jackson? 생성

    // #1. 기본방식
    @PostMapping("/request-body-json-v1")
    // HttpServletRequest를 사용해서 직접 HTTP 메시지 바디에서 데이터를 읽어와서, 문자로 변환
    // 파라미터 = 서블릿 방식
    public void requestBodyJsonV1(HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        // input stream으로 http body message 받기
        ServletInputStream inputStream = request.getInputStream();
        // input stream 문자열, 인코딩 하기
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        // 로그 출력
        log.info("messageBody={}", messageBody);

        // jackson 사용 , setter getter 만들어줌
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);
        // 객체화된 자바빈 개념 값 로그 출력
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        // 브라우저에 값 출력
        response.getWriter().write("ok");
    }

    // #2. requestBodyJsonV2 - @RequestBody 문자 변환

    // 메시지 바디 정보 직접 반환 => HttpMessageConverter 사용
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    // @RequestBody => HTTP 메시지에서 데이터를 꺼내고 messageBody에 저장 => HttpMessageConverter 사용
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
        // 문자로 된 JSON 데이터인 messageBody 를 objectMapper 를 통해서 자바 객체로 변환
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);
        // 객체 꺼내오기
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        // 리턴
        return "ok";
    }

    // v2는 문자로 변환하고 다시 json으로 변환하는 과정이 불편하다. @ModelAttribute처럼 한번에 객체로 바꾸는 방법으로 V3로 변환

/*
     #3. requestBodyJsonV3 - @RequestBody 객체 변환

    [개념]
    @ HttpEntity , @RequestBody 를 사용하면 => HTTP 메시지 컨버터가 =>
       HTTP 메시지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해 줌.
       HTTP 메시지 컨버터는 문자 뿐만 아니라 JSON도 객체로 변환해주는데, 위의 V2에서 했던 작업을 대신 처리해 줌.

     @ @RequestBody는 생략 불가능
     스프링은 @ModelAttribute , @RequestParam 과 같은 해당 애노테이션을 생략시 다음과 같은 규칙을 적용.
      a. String , int , Integer 같은 단순 타입 = @RequestParam
         나머지 = @ModelAttribute (argument resolver 로 지정해둔 타입 외)
      b. 따라서 HelloData에 @RequestBody 를 생략하면 => @ModelAttribute 가 적용 됨.
         HelloData data -> @ModelAttribute HelloData data
         => 따라서 생략하면 HTTP 메시지 바디가 아니라 요청 파라미터를 처리하게 됨.
    [주의]
     HTTP 요청시에 content-type이 application/json인지 꼭! 확인
     => 그래야 JSON을 처리할 수 있는 HTTP 메시지 컨버터가 실행
*/
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    // @RequestBody => HTTP 메시지에서 데이터를 꺼내고 messageBody에 저장 => HttpMessageConverter 사용
    public String requestBodyJsonV3(@RequestBody HelloData data) { // @RequestBody 에 직접 만든 객체를 지정
        // 객체 꺼내옴
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        // 브라우저 문자열 리턴
        return "ok";
    }

    /* #4. requestBodyJsonV4 - HttpEntity 사용 = 이전 사용 참조 */

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {
        HelloData data = httpEntity.getBody();
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }

/*
    #5. requestBodyJsonV5 = SpringMvc 제공 기능들 많이 사용

    [개념설명]
    @RequestBody 요청
      JSON 요청 -> HTTP 메시지 컨버터 -> 객체
    @ResponseBody 응답
      객체 -> HTTP 메시지 컨버터 -> JSON 응답
*/
    // 응답의 경우에도 @ResponseBody 를 사용하면 해당 객체를 HTTP 메시지 바디에 직접 넣어줄 수 있음.
    // = 물론 이 경우에도 HttpEntity 사용 가능
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) { // 리턴타입 HelloData
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return data;
    }



/*
    Postman  테스트
    POST http://localhost:8282/request-body-json-v1   => 매핑 주소 변환해서 테스트할것
    raw, JSON, content-type: application/json
    {"username":"hello", "age":20}  = raw 값
*/



} // end of class
