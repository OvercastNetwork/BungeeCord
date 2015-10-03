package net.md_5.bungee.api.chat;

import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
public class TranslatableComponent extends BaseComponent
{

    private final ResourceBundle locales = ResourceBundle.getBundle( "mojang-translations/en_US" );
    private final Pattern format = Pattern.compile( "%(?:(\\d+)\\$)?([A-Za-z%]|$)" );

    /**
     * The key into the Minecraft locale files to use for the translation. The
     * text depends on the client's locale setting. The console is always en_US
     */
    private String translate;
    /**
     * The components to substitute into the translation
     */
    private List<BaseComponent> with;

    /**
     * Creates a translatable component from the original to clone it.
     *
     * @param original the original for the new translatable component.
     */
    public TranslatableComponent(TranslatableComponent original)
    {
        super( original );
        setTranslate( original.getTranslate() );
        List<BaseComponent> temp = new ArrayList<BaseComponent>();
        for ( BaseComponent baseComponent : original.getWith() )
        {
            temp.add( baseComponent.duplicate() );
        }
        setWith( temp );
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
        setWith( temp );
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
        with = components;
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
        if ( with == null )
        {
            with = new ArrayList<BaseComponent>();
        }
        with.add( component );
    }

    @Override
    protected void toPlainText(StringBuilder builder)
    {
        try
        {
            String trans = locales.getString( translate );
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
        } catch ( MissingResourceException e )
        {
            builder.append( translate );
        }

        super.toPlainText( builder );
    }

    @Override
    protected void toLegacyText(StringBuilder builder, Formatting format)
    {
        format = Formatting.inherit(format, this);

        try
        {
            String trans = locales.getString( translate );
            Matcher matcher = this.format.matcher(trans);
            int position = 0;
            int i = 0;
            while ( matcher.find( position ) )
            {
                int pos = matcher.start();
                if ( pos != position )
                {
                    addFormat( builder, format );
                    builder.append( trans.substring( position, pos ) );
                }
                position = matcher.end();

                String formatCode = matcher.group( 2 );
                switch ( formatCode.charAt( 0 ) )
                {
                    case 's':
                    case 'd':
                        String withIndex = matcher.group( 1 );
                        with.get( withIndex != null ? Integer.parseInt( withIndex ) - 1 : i++ ).toLegacyText( builder, format );
                        break;
                    case '%':
                        addFormat( builder, format);
                        builder.append( '%' );
                        break;
                }
            }
            if ( trans.length() != position )
            {
                addFormat( builder, format );
                builder.append( trans.substring( position, trans.length() ) );
            }
        } catch ( MissingResourceException e )
        {
            addFormat( builder, format );
            builder.append( translate );
        }
        super.toLegacyText( builder, format );
    }

    private void addFormat(StringBuilder builder, Formatting format)
    {
        builder.append( getColor(format) );
        if ( isBold(format) )
        {
            builder.append( ChatColor.BOLD );
        }
        if ( isItalic(format) )
        {
            builder.append( ChatColor.ITALIC );
        }
        if ( isUnderlined(format) )
        {
            builder.append( ChatColor.UNDERLINE );
        }
        if ( isStrikethrough(format) )
        {
            builder.append( ChatColor.STRIKETHROUGH );
        }
        if ( isObfuscated(format) )
        {
            builder.append( ChatColor.MAGIC );
        }
    }

    @Override
    protected void toString(List<String> fields, Set<BaseComponent> visited) {
        fields.add("translate=" + translate);

        if(getWith() != null && !getWith().isEmpty()) {
            List<String> withText = new ArrayList<String>();
            for(BaseComponent with : getWith()) {
                withText.add(with.toString(visited));
            }
            fields.add("with=[" + Joiner.on(", ").join(withText) + "]");
        }

        super.toString(fields, visited);
    }
}
