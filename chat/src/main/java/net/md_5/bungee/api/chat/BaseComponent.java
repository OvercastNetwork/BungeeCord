package net.md_5.bungee.api.chat;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Setter
@NoArgsConstructor
public abstract class BaseComponent extends Formatting {

    /**
     * Appended components that inherit this component's formatting and events
     */
    @Getter
    private List<BaseComponent> extra;

    BaseComponent(BaseComponent old)
    {
        setColor( old.getColorRaw() );
        setBold( old.isBoldRaw() );
        setItalic( old.isItalicRaw() );
        setUnderlined( old.isUnderlinedRaw() );
        setStrikethrough( old.isStrikethroughRaw() );
        setObfuscated( old.isObfuscatedRaw() );
        setClickEvent( old.getClickEvent() );
        setHoverEvent( old.getHoverEvent() );
        if ( old.getExtra() != null )
        {
            for ( BaseComponent component : old.getExtra() )
            {
                addExtra( component.duplicate() );
            }
        }
    }

    /**
     * Clones the BaseComponent and returns the clone.
     *
     * @return The duplicate of this BaseComponent
     */
    public abstract BaseComponent duplicate();

    /**
     * Converts the components to a string that uses the old formatting codes
     * ({@link net.md_5.bungee.api.ChatColor#COLOR_CHAR}
     *
     * @param components the components to convert
     * @return the string in the old format
     */
    public static String toLegacyText(BaseComponent... components)
    {
        StringBuilder builder = new StringBuilder();
        for ( BaseComponent msg : components )
        {
            builder.append( msg.toLegacyText() );
        }
        return builder.toString();
    }

    /**
     * Converts the components into a string without any formatting
     *
     * @param components the components to convert
     * @return the string as plain text
     */
    public static String toPlainText(BaseComponent... components)
    {
        StringBuilder builder = new StringBuilder();
        for ( BaseComponent msg : components )
        {
            builder.append( msg.toPlainText() );
        }
        return builder.toString();
    }

    public void setExtra(List<BaseComponent> components)
    {
        extra = components;
    }

    /**
     * Appends a text element to the component. The text will inherit this
     * component's formatting
     *
     * @param text the text to append
     */
    public void addExtra(String text)
    {
        addExtra( new TextComponent( text ) );
    }

    /**
     * Appends a component to the component. The text will inherit this
     * component's formatting
     *
     * @param component the component to append
     */
    public void addExtra(BaseComponent component)
    {
        if ( extra == null )
        {
            extra = new ArrayList<BaseComponent>();
        }
        extra.add( component );
    }

    /**
     * Converts the component into a string without any formatting
     *
     * @return the string as plain text
     */
    public String toPlainText()
    {
        StringBuilder builder = new StringBuilder();
        toPlainText( builder );
        return builder.toString();
    }

    void toPlainText(StringBuilder builder)
    {
        if ( extra != null )
        {
            for ( BaseComponent e : extra )
            {
                e.toPlainText( builder );
            }
        }
    }

    /**
     * Converts the component to a string that uses the old formatting codes
     * ({@link net.md_5.bungee.api.ChatColor#COLOR_CHAR}
     *
     * @return the string in the old format
     */
    public String toLegacyText()
    {
        StringBuilder builder = new StringBuilder();
        toLegacyText(builder, null);
        return builder.toString();
    }

    void toLegacyText(StringBuilder builder, Formatting format)
    {
        format = Formatting.inherit(format, this);

        if ( extra != null )
        {
            for ( BaseComponent e : extra )
            {
                e.toLegacyText(builder, format);
            }
        }
    }

    protected void toString(List<String> fields, Set<BaseComponent> visited) {
        if(getColorRaw() != null) fields.add("color=" + getColorRaw());
        if(isBoldRaw() != null) fields.add("bold=" + isBoldRaw());
        if(isItalicRaw() != null) fields.add("italic=" + isItalicRaw());
        if(isUnderlinedRaw() != null) fields.add("underlined=" + isUnderlinedRaw());
        if(isStrikethroughRaw() != null) fields.add("strikethrough=" + isStrikethroughRaw());
        if(isObfuscatedRaw() != null) fields.add("obfuscated=" + isObfuscatedRaw());

        if(getClickEvent() != null) fields.add("clickEvent=" + getClickEvent());
        if(getHoverEvent() != null) fields.add("hoverEvent=" + getHoverEvent());

        if(getExtra() != null && !getExtra().isEmpty()) {
            List<String> extraText = new ArrayList<String>();
            for(BaseComponent extra : getExtra()) {
                extraText.add(extra.toString(visited));
            }
            fields.add("extra=[" + Joiner.on(", ").join(extraText) + "]");
        }
    }

    protected String toString(Set<BaseComponent> visited) {
        String text = getClass().getSimpleName() +  "{";
        if(visited.contains(this)) {
            text += "...";
        } else {
            visited = ImmutableSet.<BaseComponent>builder().addAll(visited).add(this).build();
            List<String> fields = new ArrayList<String>();
            toString(fields, visited);
            text += Joiner.on(", ").join(fields);
        }

        return text + "}";
    }

    @Override
    public String toString() {
        return toString(Collections.<BaseComponent>emptySet());
    }
}
