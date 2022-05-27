package com.rubby.hyscalper.interfaces;

public class Auction implements Comparable<Auction> {
    public String item_name;
    public String uuid;
    public String item_lore;
    public String extra;
    public String item_bytes;
    public String category;
    public String tier;
    public int starting_bid;
    public boolean bin;

    @Override
    public int compareTo(Auction o) {
        return 0;
    }
}
