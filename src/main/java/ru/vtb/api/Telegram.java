package ru.vtb.api;

import lombok.SneakyThrows;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class Telegram {

    private final static String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

    private final String apiToken="5318564950:AAHi39lLSAAuCcfB_aP4ZVQzuHnUi8wfjyA";
    private final String chatId="5214198817";

    @SneakyThrows
    public void sendToTelegram(String text) {

        String urlString = String.format(Telegram.urlString, apiToken, chatId, text);

        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            InputStream is = new BufferedInputStream(conn.getInputStream());

            ///////////////////////////////////////////////////////////////////////////////
            ///////////////////////////////////////////////////////////////////////////////
            //Additoinal Code is HERE
            ///////////////////////////////////////////////////////////////////////////////
            //getting text, we can set it to any TextView
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            StringBuilder sb = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Telegram().sendToTelegram("abcd");
    }
}
