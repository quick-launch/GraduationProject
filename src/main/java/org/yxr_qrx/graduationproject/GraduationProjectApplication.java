package org.yxr_qrx.graduationproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.yxr_qrx.graduationproject.utils.ApplicationStartup;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableJpaAuditing
@SpringBootApplication
public class GraduationProjectApplication {

    public static void main(String[] args) {
        SpringApplication springApplication=new SpringApplication(GraduationProjectApplication.class);
        springApplication.addListeners(new ApplicationStartup());
        springApplication.run(args);
    }

}
