package com.mindhub.homebanking.models;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Util {
    public String generateNumber(){
        Random random = new Random();
        int randomNumber = random.nextInt(99999999);
        return Integer.toString(randomNumber);

    }



}
