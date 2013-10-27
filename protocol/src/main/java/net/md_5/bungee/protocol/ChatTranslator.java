package net.md_5.bungee.protocol;

import java.util.List;

import lombok.Getter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChatTranslator
{
    @Getter
    public static final ChatTranslator instance = new ChatTranslator();
    @Getter
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final ImmutableMap<String, String> CHAT_MAPPING = ImmutableMap.<String, String>builder()
            .put("0", "black")
            .put("1", "dark_blue")
            .put("2", "dark_green")
            .put("3", "dark_aqua")
            .put("4", "dark_red")
            .put("5", "dark_purple")
            .put("6", "gold")
            .put("7", "gray")
            .put("8", "dark_gray")
            .put("9", "blue")
            .put("a", "green")
            .put("b", "aqua")
            .put("c", "red")
            .put("d", "light_purple")
            .put("e", "yellow")
            .put("f", "white")
            .put("r", "white").build();

    public String translate(String message)
    {
        Component input = gson.fromJson(message, Component.class);
        if ( input.text == null ) input.text = "";
        String[] chars = input.text.split("");

        List<Component> components = Lists.newArrayList();
        Component currentComponent = new Component();
        boolean parseCode = false;
        boolean canParseModifier = true;
        String recentModifier = "";

        for ( String chr : chars )
        {
            if ( chr.length() != 1 ) continue;

            if ( !parseCode && chr.equals("\u00A7") )
            {
                parseCode = true;
            } else if ( parseCode )
            {
                parseCode = false;
                String namedColor = CHAT_MAPPING.get(chr);

                // we have a normal color code, time for a new component
                if ( namedColor != null && currentComponent.text.length() > 0 )
                {
                    components.add(currentComponent);
                    currentComponent = reset(components, new Component());
                }

                if ( namedColor != null )
                {
                    currentComponent.color = namedColor;
                    canParseModifier = true;
                } else if ( recentModifier.equals( chr ) )
                {
                    addModifier(currentComponent, chr);
                } else if ( canParseModifier )
                {
                    canParseModifier = addModifier(currentComponent, chr);
                    if ( canParseModifier ) recentModifier = chr;
                } else
                {
                    // a modifier later in the string
                    components.add(currentComponent);
                    Component next = new Component();
                    next.color = currentComponent.color;
                    currentComponent = next;
                }
            } else
            {
                canParseModifier = false;
                currentComponent.text += chr;
            }
        }

        // add the last component
        components.add(currentComponent);

        Component finalComponent = null;
        for ( Component component : components )
        {
            if ( finalComponent == null ) {
                finalComponent = component;
                continue;
            }

            if ( finalComponent.extra == null ) {
                finalComponent.extra = Lists.newArrayList();
            }

            finalComponent.extra.add(component);
        }

        return gson.toJsonTree(finalComponent).toString();
    }

    private boolean addModifier(Component component, String chr)
    {
        boolean result = true;

        if (chr.equals("l")) component.bold = true;
        else if (chr.equals("o")) component.italic = true;
        else if (chr.equals("n")) component.underlined = true;
        else if (chr.equals("m")) component.strikethrough = true;
        else if (chr.equals("k")) component.obfuscated = true;
        else result = false;

        return result;
    }

    private Component reset(List<Component> components, Component next)
    {
        for ( Component old : components )
        {
            if(old.bold != null && old.bold) next.bold = false;
            if(old.italic != null && old.italic) next.italic = false;
            if(old.underlined != null && old.underlined) next.underlined = false;
            if(old.strikethrough != null && old.strikethrough) next.strikethrough = false;
            if(old.obfuscated != null && old.obfuscated) next.obfuscated = false;
        }

        return next;
    }

    private class Component
    {
        public String text = "";

        public Boolean bold;
        public Boolean italic;
        public Boolean underlined;
        public Boolean strikethrough;
        public Boolean obfuscated;
        public String color = "white";

        public List<Component> extra;
    }
}
