package us.lemin.core.api.scoreboardapi;

import org.bukkit.ChatColor;

public class ScoreboardLine {
    private static final String COLOR_CHAR_STRING = String.valueOf(ChatColor.COLOR_CHAR);
    private String text;
    private int cutPosition;

    ScoreboardLine(String text) {
        cutPosition = 16;
        setText(text);
    }

    @Override
    public String toString() {
        return text;
    }

    String getPrefix() {
        if (text.length() <= 16) {
            return text;
        }

        return text.substring(0, cutPosition);
    }

    String getPrefixFinalColor() {
        ChatColor color = ChatColor.WHITE;
        String prefix = getPrefix();
        boolean bold = false;
        boolean underlined = false;
        boolean italic = false;
        boolean strikethrough = false;
        boolean next = false;

        for (char c : prefix.toCharArray()) {
            if (next) {
                next = false;
                ChatColor charColor = ChatColor.getByChar(c);

                if (charColor == ChatColor.BOLD) {
                    bold = true;
                } else if (charColor == ChatColor.ITALIC) {
                    italic = true;
                } else if (charColor == ChatColor.UNDERLINE) {
                    underlined = true;
                } else if (charColor == ChatColor.RESET) {
                    color = ChatColor.WHITE;
                    bold = false;
                    underlined = false;
                    italic = false;
                    strikethrough = false;
                } else if (charColor == ChatColor.STRIKETHROUGH) {
                    strikethrough = true;
                } else {
                    color = charColor;
                    bold = false;
                    underlined = false;
                    italic = false;
                    strikethrough = false;
                }
            }

            if (c == ChatColor.COLOR_CHAR) {
                next = true;
            }
        }

        String finalColor = color.toString();

        if (bold) {
            finalColor += ChatColor.BOLD;
        }

        if (italic) {
            finalColor += ChatColor.ITALIC;
        }

        if (underlined) {
            finalColor += ChatColor.UNDERLINE;
        }

        if (strikethrough) {
            finalColor += ChatColor.STRIKETHROUGH;
        }

        return finalColor;
    }

    String getSuffix() {
        if (text.length() <= 16) {
            return "";
        }

        String suffix = text.substring(cutPosition);

        if (suffix.length() > 16) {
            return suffix.substring(0, 16);
        }

        return suffix;
    }

    private void setText(String text) {
        if (text.length() > 32) {
            text = text.substring(0, 32);
        }

        this.text = text;

        if (text.length() > 16) {
            String prefix = getPrefix();

            if (prefix.endsWith(COLOR_CHAR_STRING)) {
                cutPosition = 15;

                if (getSuffix().length() > 16) {
                    throw new IllegalArgumentException("text must be less than 32 characters long. This is because you have a color character in the middle.");
                }
            } else {
                cutPosition = 16;
            }
        }
    }
}
