package com.amazon.ata.cookiemonster;

class Cookie {
    private String cookieType;
    private int nomCount;

    public Cookie(String cookieType) {
        this.cookieType = cookieType;
        // First diagram points
        nomCount = getScore(cookieType);
    }

    public int getScore(String cookieType) {
        if (cookieType.equals("Chocolate Chip")) {
            return 10;
        } else if (cookieType.equals("Oatmeal Raisin")) {
            return 1;
        }
        return 5;
    }

    public int getNomCount() {
        // Fourth diagram point
        return nomCount;
    }
}
