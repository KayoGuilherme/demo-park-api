package com.ky.demo_park_api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstacionamentoUtils {

    public static String gerarRecibo() {
        LocalDateTime data = LocalDateTime.now();
        String recibo = data.toString().substring(0, 19);
        return recibo.replace("-", "").
        replace(":", "" ).
        replace("T", "-");
    }

}
