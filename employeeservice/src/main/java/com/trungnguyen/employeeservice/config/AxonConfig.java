package com.trungnguyen.employeeservice.config;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    //Config này để các service có thể gọi được nhau
    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();

        xStream.allowTypesByWildcard(new String[] {
                "com.trungnguyen.**"
        });
        return xStream;
    }
}