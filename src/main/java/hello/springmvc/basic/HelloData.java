package hello.springmvc.basic;
/* 요청 파라미터를 바인딩 받을 객체 */

import lombok.Data;

// 롬복 @Getter , @Setter , @ToString , @EqualsAndHashCode , @RequiredArgsConstructor 를 자동으로 적용
@Data
public class HelloData {
    private String username;
    private int age;
}
