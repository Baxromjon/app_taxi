package ai.ecma.server.service;

import java.util.HashSet;
import java.util.Set;

/**
 * BY BAXROMJON on 06.11.2020
 */

public class TestService {

    public static void main(String[] args) {
//        String token="Basic             bla,bla,bla       ";
//        token="kk"+token.substring("Basic".length()).trim()+"h";
//        String[] split = token.split(",",2);
//        System.out.println(Arrays.toString(split));

//        Set<Integer> integers = new HashSet<>();
//        integers.add(1);
//        integers.remove(5);
//        System.out.println(integers);

//        if (9>0||getFive()>4){
//            System.out.println("Bla");
//        }

        double f=1859300002.560274956148;
        String format = String.format("%,.0f", f).replace(","," ");
        System.out.println(format);
    }

    public static int getFive() {
        return 5;
    }
}
