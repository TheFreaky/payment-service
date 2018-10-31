package ru.kpfu.itis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);
        int instanceCount = 1;

        while (true) {

            System.out.println("1. Change count");
            System.out.println("2. View all");
            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.print("Enter instance count: ");
                instanceCount = sc.nextInt();
                Process exec = Runtime.getRuntime().exec("/usr/local/bin/docker-compose -f /home/maxim/IdeaProjects/payment-service/docker-compose.yml up -d --scale payment-service=" + instanceCount + " payment-service");
                exec.waitFor();
            } else {
                for (int i = 1; i <= instanceCount; i++) {
                    Process exec2 = Runtime.getRuntime().exec("docker inspect --format=”{{.State.Pid}}” paymentservice_payment-service_" + i);
                    exec2.waitFor();
                    int finalI = i;
                    new BufferedReader(new InputStreamReader(exec2.getInputStream())).lines().forEach(s -> System.out.println(finalI + ". Pid: " + s));
                }
            }
            System.out.println("___________\n\n\n");
        }
    }
}
