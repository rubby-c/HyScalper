package com.rubby.hyscalper.interfaces;

import java.util.ArrayList;
import java.util.List;

public class Configuration {
    public String WARNING = "Make sure you have the API keys properly formatted.";
    public List<String> API_KEYS = new ArrayList<>();
    public boolean AUTO_OPEN = true;
    public double MIN_AUTO_OPEN_PERCENT = 200;
    public int COOLDOWN = 30;
    public int THREAD_COUNT = 8;
    public List<String> WHITELIST_ITEMS = new ArrayList<>();
    public boolean IGNORE_SKINS = true;
    public boolean IGNORE_ACCESSORIES = false;
    public boolean IGNORE_PET_LEVELS = true;
    public boolean IGNORE_STARS = true;
    public boolean IGNORE_RECOMBOBULATOR = true;
    public boolean IGNORE_COUNT = true;
    public boolean IGNORE_REFORGES = true;
    public boolean IGNORE_CAKE_SOULS = true;
    public boolean IGNORE_FURNITURE = true;
    public String INFO = "SORT_BY_PROFIT: true means its gonna sort by profit, false means its gonna sort by percent.";
    public boolean SORT_BY_PROFIT = false;
    public int MIN_PRICE = 1;
    public int MAX_PRICE = 9000000;
    public int MIN_VOLUME = 15;
    public int MIN_PROFIT = 350000;
}
