package com.rubby.hyscalper.core;

import com.google.gson.Gson;
import com.nullicorn.nedit.NBTReader;
import com.nullicorn.nedit.type.NBTCompound;
import com.nullicorn.nedit.type.NBTList;
import com.rubby.hyscalper.helpers.Hit;
import com.rubby.hyscalper.helpers.Utils;
import com.rubby.hyscalper.interfaces.Api;
import com.rubby.hyscalper.interfaces.Auction;
import com.rubby.hyscalper.interfaces.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Processor {
    public static boolean ToggleBoolean = false;
    public static Gson Parser = new Gson();
    public static Configuration Configuration;
    public static final String BaseURL = "http://api.hypixel.net/skyblock/auctions?key=%s&page=";
    public static String URL;

    private static int CheckedPages;

    private static int CurrentKey = 0;
    private static int Pages = 0;

    private static ArrayList<Auction> Auctions = new ArrayList<>();
    private static ArrayList<String> ShownUuids = new ArrayList<>();
    private static Map<String, List<Auction>> SortedAuctions = new HashMap<>();

    public static void Process() throws InterruptedException, IOException {
        Pages = 0;
        CheckedPages = 0;
        Auctions = new ArrayList<>();
        SortedAuctions = new HashMap<>();

        URL = String.format(BaseURL, Configuration.API_KEYS.get(CurrentKey));

        String data = Jsoup.connect(URL + "0").ignoreContentType(true).get().text();
        Api parsed = Parser.fromJson(data, Api.class);
        if (parsed != null)
            Pages = parsed.totalPages;

        for (int i = 0; i <= Configuration.THREAD_COUNT; i++)
        {
            double perThread = Math.floor((double)Pages / Configuration.THREAD_COUNT);

            double min = i * perThread;
            double max = min + perThread;
            if (i == Configuration.THREAD_COUNT)
                max = Pages;

            Thread thread = new Thread(ProcessPage((int)min, (int)max));
            thread.start();
        }

        while (CheckedPages < Pages)
        {
            Thread.sleep(200);
        }

        for (Auction auction : Auctions) {
            String clearName = String.format("%s [%s]", Utils.PureName(auction.item_name, Configuration), auction.tier.charAt(0));

            NBTCompound baseCompound = NBTReader.readBase64(auction.item_bytes);
            NBTList baseList = baseCompound.getList("i");
            NBTCompound listCompound = baseList.getCompound(0);
            int count = listCompound.getInt("Count", 1);

            NBTCompound atr = listCompound.getCompound("tag").getCompound("ExtraAttributes");

            if (count > 1 && Configuration.IGNORE_COUNT)
                continue;

            if (atr.getString("rarity_upgrades") != null && Configuration.IGNORE_RECOMBOBULATOR) {
                if (atr.getInt("rarity_upgrades", 0) > 0)
                    continue;
            }

            if (atr.getString("modifier") != null && Configuration.IGNORE_REFORGES) {
                String modifier = atr.getString("modifier");
                String name = modifier.substring(0, 1).toUpperCase() + modifier.substring(1);
                clearName = clearName.replaceAll(name + " ", "");
            }

            if (!SortedAuctions.containsKey(clearName))
                SortedAuctions.put(clearName, new ArrayList<>());

            SortedAuctions.get(clearName).add(auction);
        }

        for (Map.Entry<String, List<Auction>> entry : SortedAuctions.entrySet())
        {
            List<Auction> sortedList = SortedAuctions.get(entry.getKey());
            sortedList.sort(Comparator.comparingInt(o -> o.starting_bid));

            SortedAuctions.replace(entry.getKey(), sortedList);
        }

        List<Hit> Hits = new ArrayList<>();

        for (Map.Entry<String, List<Auction>> entry : SortedAuctions.entrySet()) {
            if (entry.getValue().size() < Configuration.MIN_VOLUME)
                continue;

            Auction flipAuction = entry.getValue().get(0);
            Auction nextAuction = entry.getValue().get(1);

            int profit = nextAuction.starting_bid - flipAuction.starting_bid;
            double percent = (double)nextAuction.starting_bid / (double)flipAuction.starting_bid * 100;
            DecimalFormat dec = new DecimalFormat("#0.00");

            if (flipAuction.starting_bid > Configuration.MIN_PRICE &&
                    flipAuction.starting_bid < Configuration.MAX_PRICE &&
                    profit > Configuration.MIN_PROFIT)
            {
                String name = entry.getKey();
                String inMoney = Utils.InMoney(flipAuction.starting_bid, 0);
                String profitInMoney = Utils.InMoney(profit, 0);

                Hits.add(new Hit(
                        String.format("-> %s | Price: %s | Profit: %s (%s", name, inMoney, profitInMoney, dec.format(percent)) + "%)",
                        Configuration.SORT_BY_PROFIT ? profit : percent,
                        flipAuction.uuid
                ));
            }
        }

        Hits.sort(Comparator.comparingDouble(h -> h.price));

        for (Hit entry : Hits)
        {
            String cmd = "/viewauction " + entry.id;

            if (Configuration.AUTO_OPEN && !Configuration.SORT_BY_PROFIT && !ShownUuids.contains(entry.id))
            {
                if (entry.price >= Configuration.MIN_AUTO_OPEN_PERCENT)
                {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage(cmd);
                    ShownUuids.add(entry.id);
                }
            }

            Utils.SendMessage(entry.name, cmd);
        }

        Utils.SendMessage(String.format("%s<-- %sWaiting%s %s seconds %s-->", EnumChatFormatting.WHITE,
                EnumChatFormatting.GOLD, EnumChatFormatting.AQUA,
                Configuration.COOLDOWN, EnumChatFormatting.WHITE));

        if (Configuration.API_KEYS.size() > 1)
        {
            CurrentKey++;
            if (CurrentKey >= Configuration.API_KEYS.size())
                CurrentKey = 0;
        }

        CheckedPages++;

    }
    private static Runnable ProcessPage(int min, int max) throws IOException {
        for (int pageNum = min; pageNum < max; pageNum++)
        {
            String data = DownloadString(URL + pageNum);
            Api page = new Gson().fromJson(data, Api.class);

            page.auctions.removeIf(a -> !a.bin);

            if (Configuration.IGNORE_ACCESSORIES)
                page.auctions.removeIf(a -> a.category != "accessories");

            if (Configuration.IGNORE_CAKE_SOULS)
                page.auctions.removeIf(a -> a.item_lore.contains("Cake Soul"));

            if (Configuration.IGNORE_SKINS)
                page.auctions.removeIf(a -> a.item_name.contains("Skin"));

            if (Configuration.IGNORE_FURNITURE)
                page.auctions.removeIf(a -> a.item_lore.contains("Furniture"));

            if (Configuration.WHITELIST_ITEMS.size() > 0)
            {
                for (int i = 0; i < Configuration.WHITELIST_ITEMS.size(); i++)
                {
                    int index = i;
                    if (Configuration.WHITELIST_ITEMS.get(i).contains("|"))
                    {
                        String[] whitelistData = Configuration.WHITELIST_ITEMS.get(i).split("|");

                        page.auctions.removeIf(a -> a.item_name != whitelistData[0] ||
                                !a.extra.contains(whitelistData[0]) ||
                                !a.item_lore.contains(whitelistData[1]));
                    }
                    else
                    {
                        page.auctions.removeIf(a -> a.item_name != Configuration.WHITELIST_ITEMS.get(index));
                    }
                }
            }

            Auctions.addAll(page.auctions);

            CheckedPages++;
        }

        return null;
    }

    public static String DownloadString(String url) throws IOException {
        return Jsoup.connect(url).ignoreContentType(true).get().text();
    }
}
