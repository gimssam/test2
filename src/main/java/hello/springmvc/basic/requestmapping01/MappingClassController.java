package hello.springmvc.basic.requestmapping01;
/*
    #.요청 매핑 - API 예시
     : 회원 관리를 위한 HTTP API로 만든다 생각하고 매핑을 어떻게 하는지 알아봄
       (실제 데이터가 넘어가는 부분은 생략하고 URL 매핑만)

    #. 회원 관리 API 예시
        a. 회원 목록 조회: GET        /users
        b. 회원 등록: POST          /users
        c. 회원 조회: GET           /users/{userId}  {}템플릿 data 동적으로 값 넣어줌
        d. 회원수정: PATCH          /users/{userId}
        f. 회원 삭제: DELETE        /users/{userId}
*/
import org.springframework.web.bind.annotation.*;

@RestController
//클래스 레벨에 매핑 정보를 두면 메서드 레벨에서 해당 정보를 조합해서 사용 | /mapping :는 강의의 다른 예제들과 구분하기 위해 사용(빼구 써도 됨)
@RequestMapping("/mapping/users")  // 이렇게 하면 매핑 공통 default값이 되어서 /변경되는매핑명 /{userId} 만 적어주면 됨
public class MappingClassController {
    /* GET /mapping/users */
    @GetMapping
    public String users() {
        return "get users";
    }
    /* POST /mapping/users */
    @PostMapping
    public String addUser() {
        return "post user";
    }
    /* GET /mapping/users/{userId} */
    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId) {
        return "get userId=" + userId;
    }
    /* PATCH /mapping/users/{userId} */
    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId) {
        return "update userId=" + userId;
    }
    /* DELETE /mapping/users/{userId} */
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "delete userId=" + userId;
    }
}
/*
     #. Postman으로 테스트 => /mapping :는 강의의 다른 예제들과 구분하기 위해 사용(빼구 써도 됨)
        => 각 요청 방식별로 테스트해보면 return 값 문자열 나옴
        회원 목록 조회: GET /mapping/users
        회원 등록: POST /mapping/users
        회원 조회: GET /mapping/users/id1
        회원 수정: PATCH /mapping/users/id1
        회원 삭제: DELETE /mapping/users/id1
*/