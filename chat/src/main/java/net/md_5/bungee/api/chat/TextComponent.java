package net.md_5.bungee.api.chat;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
public class TextComponent extends BaseComponent
{

    private static final Pattern url = Pattern.compile( "^(?:(https?)://)?([-\\w_\\.]{2,}\\.[a-z]{2,4})(/\\S*)?$" );

    public static BaseComponent[] fromLegacyArray(String[] legacies, boolean autolink) {
        final BaseComponent[] components = new BaseComponent[legacies.length];
        for(int i = 0; i < legacies.length; i++) {
            components[i] = fromLegacyToComponent(legacies[i], autolink);
        }
        return components;
    }

    /**
     * Calls {@link #fromLegacyText(String, boolean)} with autolink true
     */
    public static BaseComponent[] fromLegacyText(String message) {
        return fromLegacyText(message, true);
    }

    /**
     * Converts the old formatting system that used
     * {@link net.md_5.bungee.api.ChatColor#COLOR_CHAR} into the new json based
     * system.
     *
     * @param message the text to convert
     * @param autolink detect links and make them clickable
     * @return the components needed to print the message to the client
     */
    public static List<BaseComponent> fromLegacyToList(String message, boolean autolink)
    {
        ArrayList<BaseComponent> components = new ArrayList<BaseComponent>();
        StringBuilder builder = new StringBuilder();
        TextComponent component = new TextComponent();
        Matcher matcher = url.matcher( message );
        boolean formatting = false;

        for ( int i = 0; i < message.length(); i++ )
        {
            final char c = message.charAt( i );
            if(c == ChatColor.COLOR_CHAR) {
                formatting = true;
            } else if(formatting) {
                formatting = false;
                final ChatColor format = ChatColor.getByChar(Character.toLowerCase(c));
                if(format != null) {
                    if(builder.length() > 0) {
                        TextComponent old = component;
                        component = new TextComponent( old );
                        old.setText( builder.toString() );
                        builder = new StringBuilder();
                        components.add( old );
                    }

                    switch ( format )
                    {
                        case BOLD:
                            component.setBold( true );
                            break;
                        case ITALIC:
                            component.setItalic( true );
                            break;
                        case UNDERLINE:
                            component.setUnderlined( true );
                            break;
                        case STRIKETHROUGH:
                            component.setStrikethrough( true );
                            break;
                        case MAGIC:
                            component.setObfuscated( true );
                            break;
                        default:
                            component = new TextComponent();
                            component.setColor( format );
                            break;
                    }
                }
            } else {
                if(autolink) {
                    int pos = message.indexOf(' ', i);
                    if(pos == -1) {
                        pos = message.length();
                    }

                    if(matcher.region(i, pos).find()) { //Web link handling
                        if(builder.length() > 0) {
                            TextComponent old = component;
                            component = new TextComponent(old);
                            old.setText(builder.toString());
                            builder = new StringBuilder();
                            components.add(old);
                        }

                        final String urlString = message.substring(i, pos);
                        final TextComponent urlComponent = new TextComponent(component);
                        urlComponent.setText(urlString);
                        urlComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
                                                                  urlString.startsWith("http") ? urlString : "http://" + urlString));
                        components.add(urlComponent);
                        i = pos - 1;
                        continue;
                    }
                }

                builder.append( c );
            }
        }

        if ( builder.length() > 0 )
        {
            component.setText( builder.toString() );
            components.add( component );
        }

        return components;
    }

    public static BaseComponent[] fromLegacyText(String message, boolean autolink) {
        final List<BaseComponent> components = fromLegacyToList(message, autolink);

        // The client will crash if the array is empty
        if ( components.isEmpty() )
        {
            components.add( new TextComponent( "" ) );
        }

        return components.toArray( new BaseComponent[ components.size() ] );
    }

    public static BaseComponent fromLegacyToComponent(String message, boolean autolink) {
        final List<BaseComponent> components = fromLegacyToList(message, autolink);
        switch(components.size()) {
            case 0: return new TextComponent();
            case 1: return components.get(0);
            default: return new TextComponent(components);
        }
    }

    /**
     * The text of the component that will be displayed to the client
     */
    @NonNull private String text;

    /**
     * Creates a blank component
     */
    public TextComponent() {
        this("");
    }

    /**
     * Creates a TextComponent with formatting and text from the passed
     * component
     *
     * @param textComponent the component to copy from
     */
    public TextComponent(TextComponent textComponent)
    {
        super( textComponent );
        setText( textComponent.getText() );
    }

    /**
     * Creates a TextComponent with blank text and the extras set to the passed
     * array
     *
     * @param extras the extras to set
     */
    public TextComponent(BaseComponent... extras)
    {
        setText( "" );
        setExtra( extras );
    }

    public TextComponent(List<BaseComponent> extra) {
        this("", extra);
    }

    public TextComponent(String text, List<BaseComponent> extra) {
        setText(text);
        setExtra(extra);
    }

    /**
     * Creates a duplicate of this TextComponent.
     *
     * @return the duplicate of this TextComponent.
     */
    @Override
    public BaseComponent duplicate()
    {
        return new TextComponent( this );
    }

    @Override
    protected void toPlainText(StringBuilder builder)
    {
        builder.append( text );
        super.toPlainText( builder );
    }

    @Override
    protected void toLegacyTextContent(ChatStringBuilder builder, ChatColor color, Set<ChatColor> decorations)
    {
        builder.format(color, decorations);
        builder.append( text );
    }

    @Override protected void toStringFirst(List<String> fields) {
        fields.add("text=\"" + getText() + "\"");
        super.toStringFirst(fields);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), text);
    }

    @Override
    protected boolean equals(BaseComponent that) {
        return that instanceof TextComponent &&
               text.equals(((TextComponent) that).getText()) &&
               super.equals(that);
    }
}
