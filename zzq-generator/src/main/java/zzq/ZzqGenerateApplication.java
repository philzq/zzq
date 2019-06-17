package zzq;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import zzq.service.GeneratorService;
import zzq.service.Impl.GeneratorServiceImpl;

@SpringBootApplication
public class ZzqGenerateApplication {

    public static void main(String[] args) {
        GeneratorService generatorService = new GeneratorServiceImpl();
        generatorService.generateCode();
    }

}
