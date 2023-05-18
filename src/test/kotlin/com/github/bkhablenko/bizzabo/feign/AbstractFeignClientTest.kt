package com.github.bkhablenko.bizzabo.feign

import com.github.bkhablenko.bizzabo.config.FeignConfig
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.openfeign.FeignAutoConfiguration

@SpringBootTest(classes = [FeignConfig::class])
@ImportAutoConfiguration(
    FeignAutoConfiguration::class,
    HttpMessageConvertersAutoConfiguration::class,
)
abstract class AbstractFeignClientTest
