package hello.springmvc.basic.requestmapping01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/*
    # 요청매핑 = 요청이 왔을때 어떤 컨트롤로가 호출이 되야돼 에 대한 것임 | 여러 요소를 조합해서 매핑을 하게 됨
(복습)
@Controller
  => 반환 값이 String 이면 뷰 이름으로 인식된다. 그래서 뷰를 찾고 뷰가 랜더링 됨
@RestController
  => 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력.
  => 따라서 실행 결과로 브라우저에서 ok 메세지를 받을 수 있음.
  => @ResponseBody 와 관련이 있는데, 뒤에서 더 자세히 설명
*/
@RestController
public class MappingController01 {
    // 로그에 출력되도록 함
    private Logger log = LoggerFactory.getLogger(getClass());
/*
    기본 요청
    둘다 허용 /hello-basic, /hello-basic/  => 마지막 /가 다름
    HTTP 메서드 모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE
    => (설정이 없다면) Postman으로 테스트 해보면 => http://localhost:8282/hello-basic => 어떤 방식이든 OK 되어 허용된다는걸 알수 있음
*/
    /* 1. 주소 url로 요청매핑 */
    @RequestMapping("/hello-basic")
//    @RequestMapping({"/hello-basic","/hello-go"}) // or 조건처럼 배열로 제공되므로 둘중하나로 url 접근 가능
    /* 0. 요청시 실행할 메서드 작성 */
    public String helloBasic(){
        log.info("hellobasic"); // 이젠 로그로 출력 | sout 안씀
        return "ok"; // 브라우저 문자열 출력
    }

    /* 2. 주소 url로 요청매핑, method 지정 */
    // (요청방식을 설정한다면) Postman으로 테스트 해보면 => Get방식만 OK 되어 => Get만 허용되는 걸 알수있음
    // @RequestMapping 에 method 속성으로 HTTP 메서드를 지정하지 않으면 HTTP 메서드와 무관하게 호출.
    // => 모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE
    // => 만약 여기에 POST 요청을 하면 스프링 MVC는 HTTP 405 상태코드(Method Not Allowed)를 반환
    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
    // 호출 메서드
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "ok";
    }

    /* 3. 주소 url로 요청매핑, method 지정 */
/*
    편리한 축약 애노테이션 = HTTP 메서드를 축약한 애노테이션을 사용하는 것이 더 직관적 임.
    => 코드를 보면 내부에서 @RequestMapping 과 method 를 지정해서 사용하는 것을 확인할 수 있음.
    => ctrl+클릭으로 구조 소스 볼수 있음 | GetMapping.java 나옴
 * @GetMapping
 * @PostMapping
 * @PutMapping
 * @DeleteMapping
 * @PatchMapping
 */
    @GetMapping(value = "/mapping-get-v2")
    // 실행 메서드
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }

/* 4. PathVariable(경로 변수) 사용 */
/*
    변수명이 같으면 생략 가능
    @PathVariable("userId") String userId -> @PathVariable userId
*/
    // 요청 url이 /mapping/userA 이런식으로 온다 => url자체에 값이 있는것(@PathVariable에 설정해줌)
    @GetMapping("/mapping/{userId}")  //{}이게 템플릿화 한것임
    public String mappingPath(@PathVariable("userId") String data) { // int a 개념
        // 주소창에서 /mapping/코딩주소명이 로그 콘솔에 찍힘
        // => 포스트맨 테스트(get방식) http://localhost:8282/mapping/userA = : (콘솔)mappingPath userId=userA
        log.info("mappingPath userId={}", data);
        return "ok";
    }
/*
    @RequestMapping 은 URL 경로를 템플릿화 할 수 있는데, @PathVariable 을 사용하면 매칭 되는 부분을 편리하게 조회할 수 있다.
    @PathVariable 의 이름과 파라미터 이름이 같으면 생략할 수 있다.
    [코드예시]
    @GetMapping("/mapping/{userId}")  {userId}와 @PathVariable("userId") 같으면 생략가능
    public String mappingPath(@PathVariable String userId) {
        log.info("mappingPath userId={}", userId);
    }
*/

    /* 5. PathVariable(경로 변수) 사용 */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    // 실행 메서드
    public String mappingPath(@PathVariable String userId, @PathVariable Long
            orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }
    // 실행
    // http://localhost:8282/mapping/users/userA/orders/100
    // => 로그콘솔 = mappingPath userId=userA, orderId=100
}

/*
    위의 매핑 이외에도 아래의 매핑 방법이 있음 => 참조만

   * 파라미터로 추가 매핑
   * params="mode",
   * params="!mode"
   * params="mode=debug"
   * params="mode!=debug" (! = )
   * params = {"mode=debug","data=good"}

   * 특정 헤더로 추가 매핑
   * headers="mode",
   * headers="!mode"
   * headers="mode=debug"
   * headers="mode!=debug" (! = )

    * Content-Type 헤더 기반 추가 매핑 Media Type
    * consumes="application/json"
    * consumes="!application/json"
    * consumes="application/*"
    * consumes="*\/*"
    * MediaType.APPLICATION_JSON_VALUE
    HTTP 요청의 Accept 헤더를 기반으로 미디어 타입으로 매핑한다.
    만약 맞지 않으면 HTTP 406 상태코드(Not Acceptable)을 반환한다.
*/