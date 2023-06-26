package hello.springmvc.basic;
/*
    # 로깅 간단히 알아보기
    : 앞으로 로그 사용
      => 운영 시스템에서는 System.out.println() 같은 시스템 콘솔을 사용해서 필요한 정보를 출력하지 않고,
         별도의 로깅 라이브러리를 사용해서 로그를 출력.
      [참고] 수많은 로그 관련 라이브러리가 있기때문에 , 여기서는 최소한의 사용방법만 알아봄

    #. 로깅 라이브러리
    : 스프링부트 라이브러리를 사용하면 => 스프링부트 로깅 라이브러리( spring-boot-starter-logging )가 함께 포함됨.
      스프링 부트 로깅 라이브러리는 기본으로 => SLF4J , LogBack 사용

    #[ SLF4J ] 라이브러리
    : 로그 라이브러리는 Logback, Log4J, Log4J2등등 있는데, 그것을 통합하여 인터페이스로 제공하는 것임
      쉽게 말해 SLF4J => 인터페이스, Logback => 구현체로 Logback 로그 라이브러리 사용.= 대부분 이거 사용
*/


// 반드시 이 패키지에 있는거 import
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Slf4j = 롬복 사용 가능 => private final Logger log = LoggerFactory.getLogger(getClass()); 사용시 코딩 안해줘도 됨

// 문자열을 리턴하면 그냥 문자열을 출력해주는 애노테이션 = 반환 값으로 뷰를 찾는 것이 아니라, HTTP 메시지 바디에 바로 입력
// | @Controller = View이름을 반환해줌
@RestController
public class LogTestController {
    // 로그 선언 = 내 클래스 지정해주면 됨
    private final Logger log = LoggerFactory.getLogger(getClass());

    // 메서드 매핑하기
    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";
        // 실행 테스트 = http://localhost:8282/log-test
        // 이전 방식임 => console에 그냥 값만 찍힘 => 각각 서버에 필요한 로그만 남기면 되는데 이건 모두 출력이 되므로 로그 폭탄 맞음
        System.out.println("name => " + name);
        // 이걸 로그로 쌓아둬야 함
        log.info(" info log={}", name); // 로그로 찍으면 console에 많은 정보를 보여줌 => 버그 찾기 쉬움

        /* 로그들의 레벨을 정할수 있다. 아래의 순서대로 심각성을 나타냄 */
        log.trace("trace log={}", name);  // 로컬서버에서 주로 보는것
        log.debug("debug log={}", name); // 디버그 = 개발서버에서 보는거다
        // application.properties 설정 없이 => 실행해보면 콘솔에 아래 3개 정보만 나옴
        log.info(" info log={}", name);  // 현재 로그의 중요한 정보를 보여줘 = 비지니스 로직이나 운영서버에서 보는 정보야
        log.warn(" warn log={}", name);  // 경고야 위험하니까 참고함
        log.error("error log={}", name); // 위험, 에러야..알람을 보거나
        // 개발시 모든 로그를 보고 싶으면 resources > application.properties에 설정해줌 = 설정해놨으니 참조

        // (주의) 로그를 사용하지 않아도 a+b 계산 로직이 먼저 실행됨, 이런 방식으로 사용하면 X
        // => 그래서 info log={}붙여줘야 함 => 이건 파라미터를 넘겨주는 단순 trace 메서드 호출임=> 어, 이거 trace구나..(아무런 연산이 일어나지 않음)
        log.debug("String concat log=" + name);
        // 문자열 리턴 = 브라우저에 출력됨
        return "ok";
    }
}
/*
    # 로그 테스트
      a. 로그가 출력되는 포멧 확인
        => (로그출력내용 )시간, 로그 레벨, 프로세스 ID, 쓰레드 명, 클래스명, 로그 메시지
      b. 로그 레벨 설정을 변경해서 출력 결과 = 레벨 설정 하위의 로그만 나옴 (예)application.properties에서 info 설정시 info,warn,error
        => LEVEL: TRACE > DEBUG > INFO > WARN > ERROR
      c. 개발 서버 => debug 출력되도록 대체로 세팅
         운영 서버 => info 출력되도록 대체로 세팅
*/
/*
    # 로그 사용시 장점
      a. 쓰레드 정보, 클래스 이름 같은 부가 정보를 함께 볼 수 있고, 출력 모양을 조정할 수 있음.
      b. 로그 레벨에 따라 개발 서버에서는 모든 로그를 출력하고, 운영서버에서는 출력하지 않는 등 로그를 상황에 맞게 조절할 수 있음.
         => application.properties에 설정만으로 가능
      c. 시스템 아웃 콘솔에만 출력하는 것이 아니라, 파일이나 네트워크 등, 로그를 별도의 위치에 남길 수 있음. (중요)
         특히 파일로 남길 때는 일별, 특정 용량에 따라 로그를 분할하는 것도 가능.
         성능도 일반 System.out보다 좋다. (내부 버퍼링, 멀티 쓰레드 등등) 그래서 실무에서는 꼭 로그를 사용해야 함
*/

