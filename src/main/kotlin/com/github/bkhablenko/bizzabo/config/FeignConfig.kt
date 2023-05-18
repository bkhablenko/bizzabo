package com.github.bkhablenko.bizzabo.config

import com.github.bkhablenko.bizzabo.feign.TvmazeClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(clients = [TvmazeClient::class])
class FeignConfig
