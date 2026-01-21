package com.example.baseservice.controller.api;

import com.example.baseservice.controller.ResponseController;
import com.example.baseservice.dto.in.TranslationIn;
import com.example.baseservice.dto.out.TranslationOut;
import com.example.baseservice.service.ITranslationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/translations")
public class TranslationRestController extends ResponseController {

    private final HttpServletRequest request;
    private final ITranslationService translationService;

    @GetMapping("/trans")
    public DeferredResult<ResponseEntity<?>> translate(@Valid TranslationIn input) {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]---> path: {} req: {}", request.getRequestURI(), input);
            TranslationOut rs = translationService.translate(input);
            log.info("[RESPONSE]---> rs: {}", rs);
            return rs;
        });
    }

    @GetMapping("/histories")
    public DeferredResult<ResponseEntity<?>> fetchHistories() {
        return responseEntityDeferredResult(() -> {
            log.info("[REQUEST]---> path: {}", request.getRequestURI());
            List<TranslationOut> rs = translationService.fetchHistories();
            log.info("[RESPONSE]---> rs: {}", rs);
            return rs;
        });
    }

}
