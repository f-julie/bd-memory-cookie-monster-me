package com.amazon.ata.cookiemonster;

import java.util.List;
import java.util.ArrayList;

class CookieMonster {
    private List<Cookie> cookies;

    public CookieMonster() {
        Cookie c1 = new Cookie("Chocolate Chip");
        // Second diagram point
        cookies = new ArrayList<>();
        cookies.add(c1);
    }

    public void eatCookie() {
        Cookie c = cookies.get(0);
        reportOnCookie(c);
        cookies.remove(c);
        // Fifth diagram point
        System.out.print("Om");
        for (int i = 0; i < c.getNomCount(); i++) {
            System.out.print(" nom");
        }
        System.out.println("!");
    }

    public void reportOnCookie(Cookie c) {
        if (c.getNomCount() < 3) {
            System.out.println("Me no like this cookie..."+
                    "just kidding.  Me love all cookies!");
        } else {
            System.out.println("Oh me like this cookie!");
        }
    }
}
