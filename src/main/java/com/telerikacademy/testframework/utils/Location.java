package com.telerikacademy.testframework.utils;

import lombok.Getter;

@Getter
public enum Location {

    SOFIA("Sofia", 1),
    PLOVDIV("Plovdiv", 2),
    VARNA("Varna", 3),
    BURGAS("Burgas", 4),
    RUSE("Ruse", 5),
    STARA_ZAGORA("Stara Zagora", 6),
    PLEVEN("Pleven", 7),
    SLIVEN("Sliven", 8),
    DOBRICH("Dobrich", 9),
    SHUMEN("Shumen", 10),
    PERNIK("Pernik", 11),
    HASKOVO("Haskovo", 12),
    VRATSA("Vratsa", 13),
    KYUSTENDIL("Kyustendil", 14),
    MONTANA("Montana", 15),
    LOVECH("Lovech", 16),
    RAZGRAD("Razgrad", 17),
    BORINO("Borino", 18),
    MADAN("Madan", 19),
    ZLATOGRAD("Zlatograd", 20),
    PAZARDZHIK("Pazardzhik", 21),
    SMOLYAN("Smolyan", 22),
    BLAGOEVGRAD("Blagoevgrad", 23),
    NEDELINO("Nedelino", 24),
    RUDOZEM("Rudozem", 25),
    DEVIN("Devin", 26),
    VELIKO_TARNOVO("Veliko Tarnovo", 27),
    VIDIN("Vidin", 28),
    KIRKOVO("Kirkovo", 29),
    KRUMOVGRAD("Krumovgrad", 30),
    DZHEBEL("Dzhebel", 31),
    SILISTRA("Silistra", 32),
    SARNITSA("Sarnitsa", 33),
    SHIROKA_LAKA("Shiroka Laka", 34),
    YAMBOL("Yambol", 35),
    DOSPAT("Dospat", 36),
    KARDZHALI("Kardzhali", 37),
    GABROVO("Gabrovo", 38),
    TARGOVISHTE("Targovishte", 39);

    public final String stringValue;
    public final int id;

    Location(String stringValue, int id) {
        this.stringValue = stringValue;
        this.id = id;
    }
}