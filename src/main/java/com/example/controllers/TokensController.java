package com.example.controllers;

import com.example.models.TokensResponse;
import com.example.services.TokensService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/tokens")
@RestController
public class TokensController {
    Logger logger = LoggerFactory.getLogger(TokensController.class);

    private final TokensService tokensService;

    public TokensController(TokensService tokensService) {
        this.tokensService = tokensService;
    }

    @GetMapping
    public String test() {
        logger.info("test");
        return "OK";
    }

    @PostMapping("/redeem")
    public TokensResponse redeemToken(@RequestBody String jws) {
        logger.info("Redeeming '{}'", jws);
        return tokensService.redeemToken(jws);
    }

    @PostMapping("/validate")
    public TokensResponse validateToken(@RequestBody String jws) {
        logger.info("Validating '{}'", jws);
        return tokensService.validateToken(jws);
    }

    @DeleteMapping
    public void resetToken(@RequestBody String jws) {
        logger.info("Resetting '{}'", jws);
        tokensService.resetToken(jws);
    }


}
