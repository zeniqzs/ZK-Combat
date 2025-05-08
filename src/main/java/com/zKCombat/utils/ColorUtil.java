package com.zKCombat.utils;

import net.md_5.bungee.api.ChatColor;

public class ColorUtil {

    public static String parseMessage(String message) {
        return parseColors(message);
    }

    public static String parseColors(String message) {
        if (message == null) {
            return "";
        }

        StringBuilder parsedMessage = new StringBuilder();
        int length = message.length();

        for (int i = 0; i < length; i++) {
            char currentChar = message.charAt(i);

            if (currentChar == '&' && i + 1 < length) {
                char colorChar = message.charAt(i + 1);
                ChatColor color = ChatColor.getByChar(colorChar);
                if (color != null) {
                    parsedMessage.append(color);
                    i++;
                    continue;
                }
            }

            if (currentChar == '#' && i + 6 < length) {
                String colorCode = message.substring(i, i + 7);
                try {
                    ChatColor chatColor = ChatColor.of(colorCode);
                    parsedMessage.append(chatColor);
                    i += 6;
                    continue;
                } catch (IllegalArgumentException e) {
                    parsedMessage.append(currentChar);
                }
            } else {
                parsedMessage.append(currentChar);
            }
        }

        return parsedMessage.toString();
    }
}
