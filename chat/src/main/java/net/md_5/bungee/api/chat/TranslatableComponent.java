package net.md_5.bungee.api.chat;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatStringBuilder;

@Getter
@Setter
public class TranslatableComponent extends BaseComponent
{

    private static final ResourceBundle locales = ResourceBundle.getBundle( "mojang-translations/en_US" );
    private static final Pattern format = Pattern.compile( "%(?:(\\d+)\\$)?([A-Za-z%]|$)" );

    /**
     * The key into the Minecraft locale files to use for the translation. The
     * text depends on the client's locale setting. The console is always en_US
     */
    @NonNull private String translate;

    /**
     * The components to substitute into the translation
     */
    private List<BaseComponent> with = EMPTY_COMPONENT_LIST;

    /**
     * Creates a translatable component from the original to clone it.
     *
     * @param original the original for the new translatable component.
     */
    public TranslatableComponent(TranslatableComponent original)
    {
        super( original );
        setTranslate( original.getTranslate() );

        if ( original.getWith() != null )
        {
            List<BaseComponent> temp = new ArrayList<BaseComponent>();
            for ( BaseComponent baseComponent : original.getWith() )
            {
                temp.add( baseComponent.duplicate() );
            }
            setWithInternal( temp );
        }
    }

    /**
     * Creates a translatable component with the passed substitutions
     *
     * @see #translate
     * @see #setWith(java.util.List)
     * @param translate the translation key
     * @param with the {@link java.lang.String}s and
     * {@link net.md_5.bungee.api.chat.BaseComponent}s to use into the
     * translation
     */
    public TranslatableComponent(String translate, Object... with)
    {
        setTranslate( translate );
        List<BaseComponent> temp = new ArrayList<BaseComponent>();
        for ( Object w : with )
        {
            if ( w instanceof String )
            {
                temp.add( new TextComponent( (String) w ) );
            } else
            {
                temp.add( (BaseComponent) w );
            }
        }
        setWithInternal( temp );
    }

    /**
     * Creates a duplicate of this TranslatableComponent.
     *
     * @return the duplicate of this TranslatableComponent.
     */
    @Override
    public BaseComponent duplicate()
    {
        return new TranslatableComponent( this );
    }

    /**
     * Sets the translation substitutions to be used in this component. Removes
     * any previously set substitutions
     *
     * @param components the components to substitute
     */
    public void setWith(List<BaseComponent> components)
    {
        if(components == null) {
            setWithInternal(null);
        } else {
            for(BaseComponent child : components) validateChild(child);
            setWithInternal(new ArrayList<BaseComponent>(components));
        }
    }

    /**
     * Sets the translation substitutions to be used in this component. Removes
     * any previously set substitutions
     *
     * @param components the components to substitute
     */
    public void setWith(BaseComponent... components)
    {
        setWith(components == null ? EMPTY_COMPONENT_LIST : Arrays.asList(components));
    }

    private void setWithInternal(List<BaseComponent> components) {
        with = components == null || components.isEmpty() ? EMPTY_COMPONENT_LIST : components;
    }

    /**
     * Adds a text substitution to the component. The text will inherit this
     * component's formatting
     *
     * @param text the text to substitute
     */
    public void addWith(String text)
    {
        addWith( new TextComponent( text ) );
    }

    /**
     * Adds a component substitution to the component. The text will inherit
     * this component's formatting
     *
     * @param component the component to substitute
     */
    public void addWith(BaseComponent component)
    {
        validateChild(component);
        if ( with == EMPTY_COMPONENT_LIST )
        {
            with = new ArrayList<BaseComponent>();
        }
        with.add( component );
    }

    @Override
    protected void toPlainText(StringBuilder builder)
    {
        String trans;
        try
        {
            trans = locales.getString( translate );
        } catch ( MissingResourceException ex )
        {
            trans = translate;
        }

        Matcher matcher = format.matcher( trans );
        int position = 0;
        int i = 0;
        while ( matcher.find( position ) )
        {
            int pos = matcher.start();
            if ( pos != position )
            {
                builder.append( trans.substring( position, pos ) );
            }
            position = matcher.end();

            String formatCode = matcher.group( 2 );
            switch ( formatCode.charAt( 0 ) )
            {
                case 's':
                case 'd':
                    String withIndex = matcher.group( 1 );
                    with.get( withIndex != null ? Integer.parseInt( withIndex ) - 1 : i++ ).toPlainText( builder );
                    break;
                case '%':
                    builder.append( '%' );
                    break;
            }
        }
        if ( trans.length() != position )
        {
            builder.append( trans.substring( position, trans.length() ) );
        }

        super.toPlainText( builder );
    }

    @Override
    protected void toLegacyTextContent(ChatStringBuilder builder, ChatColor color, Set<ChatColor> decorations)
    {
        String trans;
        try
        {
            trans = locales.getString( translate );
        } catch ( MissingResourceException e )
        {
            trans = translate;
        }

        Matcher matcher = format.matcher( trans );
        int position = 0;
        int i = 0;
        while ( matcher.find( position ) )
        {
            int pos = matcher.start();
            if ( pos != position )
            {
                builder.format( color, decorations );
                builder.append( trans.substring( position, pos ) );
            }
            position = matcher.end();

            String formatCode = matcher.group( 2 );
            switch ( formatCode.charAt( 0 ) )
            {
                case 's':
                case 'd':
                    String withIndex = matcher.group( 1 );
                    with.get( withIndex != null ? Integer.parseInt( withIndex ) - 1 : i++ ).toLegacyText( builder, color, decorations );
                    break;
                case '%':
                    builder.format( color, decorations );
                    builder.append( '%' );
                    break;
            }
        }
        if ( trans.length() != position )
        {
            builder.format( color, decorations );
            builder.append( trans.substring( position, trans.length() ) );
        }
    }

    @Override
    public boolean contains(BaseComponent child) {
        if(super.contains(child)) return true;
        for(BaseComponent with : getWith()) {
            if(with.contains(child)) return true;
        }
        return false;
    }

    @Override
    protected void toStringFirst(List<String> fields) {
        fields.add("translate=\"" + getTranslate() + "\"");
        super.toStringFirst(fields);
    }

    @Override
    protected void toStringLast(List<String> fields) {
        if(getWith() != null && !getWith().isEmpty()) {
            fields.add("with=[" + JOINER.join(getWith()) + "]");
        }
        super.toStringLast(fields);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), translate, with);
    }

    @Override
    protected boolean equals(BaseComponent that) {
        return that instanceof TranslatableComponent &&
               translate.equals(((TranslatableComponent) that).getTranslate()) &&
               super.equals(that) &&
               with.equals(((TranslatableComponent) that).getWith());
    }
}
