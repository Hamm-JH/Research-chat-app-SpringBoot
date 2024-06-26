package com.springboot.boot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

// 테스트 :: 임의의 포트로 웹 서버를 시작할 수 있음
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CheckHTTPResponse {
    // 테스트 프레임워크에서 이 필드에 임의의 포트를 삽입할 수 있습니다.
    @LocalServerPort
    private int port;

    // SpringBoot에 알림
    // 테스트 자체의 컨텍스트로 testRestTemplate를 가져옴
    // 예상 값이 동일할 때 통과하기를 원한다.
    @Autowired
    private TestRestTemplate testRestTemplate;

    // Test로 어노테이션을 달고, 예상 값이 동일할 때 통과하기르 원한다.
    @Test
    public void shouldPassIfStringMatches() {
        // 임의의 포트의 로컬 호스트에서 제공하는 문자열을 비교하는 AssertEquals문을 작성합니다.
        // testRestTemplate의 인스턴스에서 getForObject 메소드 호출을 사용하고,
        // 이를 문자열로 변환하여 사용하려는 문자열과 비교해야 합니다.
        assertEquals("Hello World from Spring Boot",
                testRestTemplate.getForObject("http://localhost:" + port + "/", String.class));

        // 마지막으로 Intellij IEA가 Junit5에 대한 가져오기를 추가하고
        // Alt+Enter를 사용하여 읽기 쉽도록 각 인수를 별도의 줄에 배치합니다.

        // 이제 테스트 클래스 선언줄로 가서 테스트를 실행합니다. (또는 Ctrl+Shift+F10)
        // 아직 Spring 컨트롤러를 구현하지 않았기 때문에 실패할 것으로 예상됩니다.
        // 실패한 테스트부터 시작하는 것은 코드가 예상한 대로 작동하는지 확인하는 좋은 방법입니다.
    }

    @Test
    public void shouldPassIsGoodbyeMatches() {
        assertEquals("Goodbye from Spring Boot",
                testRestTemplate.getForObject("http://localhost:" + port + "/goodbye", String.class));
    }

}