package springBootPickers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    InterceptorConfig interceptorConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 로그인 하지 않아도 되는 페이지를 저장
        List<String> excludeList = new ArrayList<>();
        // 공통적으로 허용할 주소
        excludeList.add("/help/findId");
        excludeList.add("/help/findPw");
        excludeList.add("/login/**/*");
        excludeList.add("/register/**/*");

        // 인터셉터 등록
        registry.addInterceptor(interceptorConfig)
                .addPathPatterns("/**/*") // 모든 요청에 대해 적용
                .excludePathPatterns(excludeList); // 허용할 주소
    }
}
