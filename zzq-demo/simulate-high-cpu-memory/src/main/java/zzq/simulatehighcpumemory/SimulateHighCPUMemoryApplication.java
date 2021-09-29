package zzq.simulatehighcpumemory;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class SimulateHighCPUMemoryApplication implements ApplicationRunner {

    public static void main(String[] args) {

        SpringApplication.run(SimulateHighCPUMemoryApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(this::cpuGo);
    }

    /**
     * 模拟高频cpu go go go
     */
    private void cpuGo() {
        int num = 0;
        long start = System.currentTimeMillis() / 1000;
        while (true) {
            num++;
            if (num == Integer.MAX_VALUE) {
                try {
                    throw new RuntimeException("cpu go go go");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                num = 0;
            }
            if ((System.currentTimeMillis() / 1000) - start > 1000) {
                return;
            }
        }
    }
}
