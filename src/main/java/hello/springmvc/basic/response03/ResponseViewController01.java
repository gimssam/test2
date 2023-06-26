package hello.springmvc.basic.response03;
/*
    HTTP 응답 - 정적 리소스, 뷰 템플릿
    : 스프링(서버)에서 응답 데이터를 만드는 방법은 크게 3가지임.
      a. 정적 리소스 = 해당 파일을 변경 없이 그대로 서비스하는 것
      : 예) 웹 브라우저에 정적인 HTML, css, js를 제공할 때는, 정적 리소스를 사용
         스프링부트 정적 리소스 경로
         = src/main/resources/static
         웹 브라우저 실행
         = http://localhost:8080/basic/hello-form.html

      b. 뷰 템플릿 사용 = 뷰 템플릿을 거쳐서 HTML이 생성되고, 뷰가 응답을 만들어서 전달
      : 예) 웹 브라우저에 동적인 HTML을 제공할 때는 뷰 템플릿을 사용
         스프링부트 뷰 템플릿 경로
         = src/main/resources/templates

      c. HTTP 메시지 사용
      : HTTP API를 제공하는 경우에는 HTML이 아니라 데이터를 전달해야 하므로,
        = HTTP 메시지 바디에 JSON 같은 형식으로 데이터를 실어 보낸다.

*/
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

// (중요) @Controller는 문자열을 리틴하면 뷰의 논리적 이름이 됨
@Controller
public class ResponseViewController01 {
    /* #1. ModelAndView 사용 */
    @RequestMapping("response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView mav = new ModelAndView("response/hello") // resources/template 아래경로 설정
                .addObject("data","hello"); // 타임리프에 data 넣어줌
        return mav;
    }

/* #2. String 반환 = @Controller일 경우 리턴 문자열은 논리적 이름이 됨 */
    // 만약, @ResponseBody 사용할 경우 body에 문자열이 출력됨 = http 응답메서지 문자열로 나감
    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) { // String 반환이므로 Model 필요
        model.addAttribute("data", "hello!!");
        return "response/hello";  // 뷰의 논리적 이름이 됨 => template > response > hello.html
    }

/* #3. Void 반환 = 권장하지 않음. 하지마 */
    @RequestMapping("/response/hello")  // 경로이름이랑 같으면 void로 반환해줄수 있다.
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!!"); }


} // end of class


