package org.medilabo.microrisque.config;

import feign.Response;
import feign.codec.ErrorDecoder;

/**
 *
 * To manage the errors
 *
 */
public class CustomErrorDecoder  implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 404:
                return new RuntimeException("Ressource non trouvée");
            case 500:
                return new RuntimeException("Erreur serveur du microservice");
            default:
                return new RuntimeException("Erreur de communication avec le microservice");
        }
    }
}
