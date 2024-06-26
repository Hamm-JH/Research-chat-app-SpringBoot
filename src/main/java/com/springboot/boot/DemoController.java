package com.springboot.boot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// Spring이 이를 어떻게 처리할지 알 수 있도록 @RestController로 어노테이션을 달아야 합니다.
@RestController
public class DemoController {

    // 우리의 메소드는 어떤 인수도 제공하지 않기 때문에 기본적으로 루트 디렉토리가 될 @Request로 어노테이션을 달아야 합니다.
    @RequestMapping
    public String helloWorld() {
        // 이 메소드는 문자열을 반환합니다.
        // 그리고 해당 문자열은 테스트 클래스에서 사용한 것과 동일한 문자열인 "Hello World from Spring Boot"가 됩니다.
        return "Hello World from Spring Boot";
    }

    // Spring Controller 클래스로 돌아가서 추가 메소드를 추가할 수 있습니다.
    // 이 메소드에 다시 요청 매핑으로 주석을 달겠지만 이번에는 슬래시를 요청 매개변수로 전달하겠습니다.
    @RequestMapping(value="/goodbye", method = RequestMethod.GET)
    public String goodbye() {
        // 우리의 새로운 메소드는 브라우저를 새로 고칠 때 변경 사항을 볼 수 있도록 문자열을 반환할 것입니다.
        // 애플리케이션을 다시 실행하여 변경 사항이 실제로 적용되는지 살펴보겠습니다.
        return "Goodbye from Spring Boot";
    }
}
