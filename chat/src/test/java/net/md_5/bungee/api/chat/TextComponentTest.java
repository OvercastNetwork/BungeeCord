package net.md_5.bungee.api.chat;

import net.md_5.bungee.api.ChatColor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextComponentTest {

    @Test
    public void fromLegacyNoFormatting() throws Exception {
        assertEquals(new TextComponent("woot"),
                     TextComponent.fromLegacyToComponent("woot", false));
    }

    @Test
    public void fromLegacyWithFormatting() throws Exception {
        TextComponent c = new TextComponent("woot");
        c.setColor(ChatColor.GREEN);
        c.setBold(true);

        assertEquals(c, TextComponent.fromLegacyToComponent(ChatColor.GREEN.toString() + ChatColor.BOLD + "woot", false));
    }

    @Test
    public void fromLegacyPartialColor() throws Exception {
        TextComponent one = new TextComponent("One");
        TextComponent two = new TextComponent("Two"); two.setColor(ChatColor.GREEN);
        TextComponent c = new TextComponent(one, two);

        assertEquals(c, TextComponent.fromLegacyToComponent("One" + ChatColor.GREEN + "Two", false));
    }

    @Test
    public void fromLegacyAutoLinkStandalone() throws Exception {
        TextComponent c = new TextComponent("https://oc.tc");
        c.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://oc.tc"));

        assertEquals(c, TextComponent.fromLegacyToComponent("https://oc.tc", true));
    }

    @Test
    public void fromLegacyAutoLinkInline() throws Exception {
        TextComponent one = new TextComponent("Go to ");
        TextComponent two = new TextComponent("https://oc.tc"); two.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://oc.tc"));
        TextComponent three = new TextComponent(" for a good time");
        TextComponent c = new TextComponent(one, two, three);

        assertEquals(c, TextComponent.fromLegacyToComponent("Go to https://oc.tc for a good time", true));
    }

    @Test
    public void toLegacyNoFormatting() throws Exception {
        assertEquals("woot", new TextComponent("woot").toLegacyText());
    }

    @Test
    public void toLegacyWithFormatting() throws Exception {
        TextComponent c = new TextComponent("woot");
        c.setColor(ChatColor.GREEN);
        c.setBold(true);

        assertEquals(ChatColor.GREEN.toString() + ChatColor.BOLD + "woot",
                     c.toLegacyText());
    }

    @Test
    public void toLegacyPartialColorNoDefault() throws Exception {
        TextComponent one = new TextComponent("One");
        TextComponent two = new TextComponent("Two"); two.setColor(ChatColor.GREEN);
        TextComponent three = new TextComponent("Three");
        one.addExtra(two);
        one.addExtra(three);

        assertEquals("One" + ChatColor.GREEN + "Two" + ChatColor.RESET + "Three",
                     one.toLegacyText());
    }

    @Test
    public void toLegacyPartialColorWithDefault() throws Exception {
        TextComponent one = new TextComponent("One");
        TextComponent two = new TextComponent("Two"); two.setColor(ChatColor.GREEN);
        TextComponent three = new TextComponent("Three");
        one.addExtra(two);
        one.addExtra(three);

        assertEquals(ChatColor.RED + "One" + ChatColor.GREEN + "Two" + ChatColor.RED + "Three",
                     one.toLegacyText(ChatColor.RED));
    }

    @Test
    public void toLegacyAddDecoration() throws Exception {
        TextComponent one = new TextComponent("One"); one.setColor(ChatColor.RED);
        TextComponent two = new TextComponent("Two"); two.setBold(true);
        one.addExtra(two);

        assertEquals(ChatColor.RED + "One" + ChatColor.BOLD + "Two",
                     one.toLegacyText());
    }

    @Test
    public void toLegacyRemoveDecoration() throws Exception {
        TextComponent one = new TextComponent("One"); one.setColor(ChatColor.RED);
        TextComponent two = new TextComponent("Two"); two.setBold(true);
        TextComponent three = new TextComponent("Three");
        one.addExtra(two);
        one.addExtra(three);

        assertEquals(ChatColor.RED + "One" + ChatColor.BOLD + "Two" + ChatColor.RED + "Three",
                     one.toLegacyText());
    }

    @Test
    public void toLegacyNestedDecoration() throws Exception {
        TextComponent one = new TextComponent("One"); one.setColor(ChatColor.RED);
        TextComponent two = new TextComponent("Two"); two.setBold(true);
        TextComponent three = new TextComponent("Three"); three.setItalic(true);
        one.addExtra(two);
        two.addExtra(three);
        two.addExtra(new TextComponent("Two"));
        one.addExtra(new TextComponent("One"));

        assertEquals(ChatColor.RED + "One" + ChatColor.BOLD + "Two" + ChatColor.ITALIC + "Three" + ChatColor.RED + ChatColor.BOLD + "Two" + ChatColor.RED + "One",
                     one.toLegacyText());
    }

    @Test
    public void toLegacyCollapseFormatting() throws Exception {
        TextComponent one = new TextComponent(""); one.setColor(ChatColor.RED);
        TextComponent two = new TextComponent(""); two.setColor(ChatColor.GREEN);
        TextComponent three = new TextComponent("Hi"); three.setColor(ChatColor.BLUE);
        TextComponent four = new TextComponent(""); four.setColor(ChatColor.YELLOW);
        TextComponent five = new TextComponent("There"); five.setColor(ChatColor.BLUE);
        one.addExtra(two);
        one.addExtra(three);
        one.addExtra(four);
        one.addExtra(five);

        assertEquals(ChatColor.BLUE + "HiThere",
                     one.toLegacyText());
    }

    @Test
    public void toLegacyWithClickEvent() throws Exception {
        TextComponent one = new TextComponent("Go to ");
        TextComponent two = new TextComponent("https://oc.tc"); two.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://oc.tc"));
        TextComponent three = new TextComponent(" for a good time");
        TextComponent c = new TextComponent(one, two, three);

        assertEquals("Go to https://oc.tc for a good time", c.toLegacyText());
    }
}
