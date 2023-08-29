package com.skytrix.OrderService.Config;

import com.skytrix.OrderService.External.Decorder.CustomErrorDecorder;
import feign.Capability;
import feign.codec.ErrorDecoder;
import feign.micrometer.MicrometerCapability;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    ErrorDecoder errorDecoder(){
        return new CustomErrorDecorder();
    }

    @Bean
    public Capability capability(final MeterRegistry registry){
        return new MicrometerCapability(registry);
    }
}
