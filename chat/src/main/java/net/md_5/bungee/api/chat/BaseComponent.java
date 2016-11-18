package net.md_5.bungee.api.chat;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

@Setter
@NoArgsConstructor
public abstract class BaseComponent
{
    /**
     * An immutable, empty component list. {@link BaseComponent#extra} is always set to this when empty.
     * Subclasses can use this for their own fields as well.
     */
    protected static final List<BaseComponent> EMPTY_COMPONENT_LIST = Collections.emptyList();

    /**
     * The color of this component and any child components (unless overridden)
     */
    private ChatColor color;
    /**
     * Whether this component and any child components (unless overridden) is
     * bold
     */
    private Boolean bold;
    /**
     * Whether this component and any child components (unless overridden) is
     * italic
     */
    private Boolean italic;
    /**
     * Whether this component and any child components (unless overridden) is
     * underlined
     */
    private Boolean underlined;
    /**
     * Whether this component and any child components (unless overridden) is
     * strikethrough
     */
    private Boolean strikethrough;
    /**
     * Whether this component and any child components (unless overridden) is
     * obfuscated
     */
    private Boolean obfuscated;
    /**
     * The text to insert into the chat when this component (and child
     * components) are clicked while pressing the shift key
     */
    @Getter
    private String insertion;

    /**
     * Appended components that inherit this component's formatting and events
     */
    @Getter
    private List<BaseComponent> extra = EMPTY_COMPONENT_LIST;

    /**
     * The action to preform when this component (and child components) are
     * clicked
     */
    @Getter
    private ClickEvent clickEvent;
    /**
     * The action to preform when this component (and child components) are
     * hovered over
     */
    @Getter
    private HoverEvent hoverEvent;

    BaseComponent(BaseComponent old)
    {
        setColor( old.getColorRaw() );
        setBold( old.isBoldRaw() );
        setItalic( old.isItalicRaw() );
        setUnderlined( old.isUnderlinedRaw() );
        setStrikethrough( old.isStrikethroughRaw() );
        setObfuscated( old.isObfuscatedRaw() );
        setInsertion( old.getInsertion() );
        setClickEvent( old.getClickEvent() );
        setHoverEvent( old.getHoverEvent() );
        for ( BaseComponent component : old.getExtra() )
        {
            addValidExtra( component.duplicate() );
        }
    }

    /**
     * Clones the BaseComponent and returns the clone.
     *
     * @return The duplicate of this BaseComponent
     */
    public abstract BaseComponent duplicate();

    public static String[] toLegacyArray(BaseComponent... components) {
        final String[] legacies = new String[components.length];
        for(int i = 0; i < components.length; i++) {
            legacies[i] = components[i].toLegacyText();
        }
        return legacies;
    }

    public static String toLegacyText(BaseComponent... components) {
        return toLegacyText(null, ImmutableSet.<ChatColor>of(), components);
    }

    /**
     * Converts the components to a string that uses the old formatting codes
     * ({@link net.md_5.bungee.api.ChatColor#COLOR_CHAR}
     *
     * @param components the components to convert
     * @return the string in the old format
     */
    public static String toLegacyText(@Nullable ChatColor color, Set<ChatColor> decorations, BaseComponent... components)
    {
        StringBuilder builder = new StringBuilder();
        for ( BaseComponent msg : components )
        {
            builder.append( msg.toLegacyText(color, decorations) );
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

    /**
     * Returns the color of this component, or {@link net.md_5.bungee.api.ChatColor#WHITE}
     * if this component has no color.
     *
     * @return the color of this component
     */
    public ChatColor getColor()
    {
        return color != null ? color : ChatColor.WHITE;
    }

    /**
     * Returns the color of this component, or null if this component has no color.
     *
     * @return the color of this component
     */
    public ChatColor getColorRaw()
    {
        return color;
    }

    public ChatColor getColor(ChatColor def) {
        return color != null ? color : def;
    }

    public void setColor(@Nullable ChatColor color) {
        Preconditions.checkArgument(color == null || color.isColor(), "not a color");
        this.color = color;
    }

    /**
     * Returns whether this component is bold. This uses the parent's setting if
     * this component hasn't been set. false is returned if none of the parent
     * chain has been set.
     *
     * @return whether the component is bold
     */
    public boolean isBold()
    {
        return bold != null && bold;
    }

    /**
     * Returns whether this component is bold without checking the parents
     * setting. May return null
     *
     * @return whether the component is bold
     */
    public Boolean isBoldRaw()
    {
        return bold;
    }

    /**
     * Returns whether this component is italic. This uses the parent's setting
     * if this component hasn't been set. false is returned if none of the
     * parent chain has been set.
     *
     * @return whether the component is italic
     */
    public boolean isItalic()
    {
        return italic != null && italic;
    }

    /**
     * Returns whether this component is italic without checking the parents
     * setting. May return null
     *
     * @return whether the component is italic
     */
    public Boolean isItalicRaw()
    {
        return italic;
    }

    /**
     * Returns whether this component is underlined. This uses the parent's
     * setting if this component hasn't been set. false is returned if none of
     * the parent chain has been set.
     *
     * @return whether the component is underlined
     */
    public boolean isUnderlined()
    {
        return underlined != null && underlined;
    }

    /**
     * Returns whether this component is underlined without checking the parents
     * setting. May return null
     *
     * @return whether the component is underlined
     */
    public Boolean isUnderlinedRaw()
    {
        return underlined;
    }

    /**
     * Returns whether this component is strikethrough. This uses the parent's
     * setting if this component hasn't been set. false is returned if none of
     * the parent chain has been set.
     *
     * @return whether the component is strikethrough
     */
    public boolean isStrikethrough()
    {
        return strikethrough != null && strikethrough;
    }

    /**
     * Returns whether this component is strikethrough without checking the
     * parents setting. May return null
     *
     * @return whether the component is strikethrough
     */
    public Boolean isStrikethroughRaw()
    {
        return strikethrough;
    }

    /**
     * Returns whether this component is obfuscated. This uses the parent's
     * setting if this component hasn't been set. false is returned if none of
     * the parent chain has been set.
     *
     * @return whether the component is obfuscated
     */
    public boolean isObfuscated()
    {
        return obfuscated != null && obfuscated;
    }

    /**
     * Returns whether this component is obfuscated without checking the parents
     * setting. May return null
     *
     * @return whether the component is obfuscated
     */
    public Boolean isObfuscatedRaw()
    {
        return obfuscated;
    }

    public void setHoverEvent(HoverEvent hoverEvent) {
        if(hoverEvent != null) {
            for(BaseComponent child : hoverEvent.getValue()) validateChild(child);
        }
        this.hoverEvent = hoverEvent;
    }

    public void setExtra(List<BaseComponent> components)
    {
        if(components == null || components.isEmpty()) {
            extra = EMPTY_COMPONENT_LIST;
        } else {
            for(BaseComponent child : components) validateChild(child);
            extra = new ArrayList<BaseComponent>(components);
        }
    }

    public void setExtra(BaseComponent... components) {
        if(components == null || components.length == 0) {
            extra = EMPTY_COMPONENT_LIST;
        } else {
            for(BaseComponent child : components) validateChild(child);
            extra = Lists.newArrayList(components);
        }
    }

    /**
     * Appends a text element to the component. The text will inherit this
     * component's formatting
     *
     * @param text the text to append
     */
    public void addExtra(String text)
    {
        addValidExtra( new TextComponent( text ) );
    }

    /**
     * Appends a component to the component. The text will inherit this
     * component's formatting
     *
     * @param component the component to append
     *                  @throws IllegalArgumentException if a component cycle is detected
     */
    public void addExtra(BaseComponent component)
    {
        validateChild( component );
        addValidExtra(component);
    }

    /**
     * Append a component without validating it. This method should only be called when
     * it is certain not to create a component cycle i.e. when the given component is
     * known not to contain this one.
     */
    private void addValidExtra(BaseComponent component) {
        if (extra == EMPTY_COMPONENT_LIST)
        {
            extra = new ArrayList<BaseComponent>();
        }
        extra.add( component );
    }

    /**
     * Returns whether the component has any formatting or events applied to it
     *
     * @return Whether any formatting or events are applied
     */
    public boolean hasFormatting()
    {
        return color != null || bold != null
                || italic != null || underlined != null
                || strikethrough != null || obfuscated != null
                || hoverEvent != null || clickEvent != null;
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
     * Calls {@link #toLegacyText(ChatColor, Set)} with no default color or decorations.
     */
    public String toLegacyText() {
        return toLegacyText(null, ImmutableSet.<ChatColor>of());
    }

    public String toLegacyText(@Nullable ChatColor color, ChatColor... decorations) {
        return toLegacyText(color, ImmutableSet.copyOf(decorations));
    }

    /**
     * Converts the component to a string that uses the old formatting codes {@link net.md_5.bungee.api.ChatColor#COLOR_CHAR}
     *
     * Any given default color or decorations will be applied as if they were inherited from a parent component.
     * Any part of the string that is not formatted by some component in this tree will have the default formatting
     * applied.
     *
     * If null is given as the default color, then no color is applied to the returned string other than from
     * the components. This is only possible up to the first formatting applied by a component (because formatting
     * cannot be "removed" in legacy text, only replaced). Any default-formatted text after that will be given
     * the {@link ChatColor#RESET} format, which can also be the color passed to this method.
     *
     * @param color         Default color, or {@link ChatColor#RESET}, or null for no default
     * @param decorations   Default decorations
     *
     * @return the string in the old format
     */
    public String toLegacyText(@Nullable ChatColor color, Set<ChatColor> decorations)
    {
        Preconditions.checkArgument(color == null || color.isColor(), "default color cannot be a decoration");

        ChatStringBuilder builder = new ChatStringBuilder();
        toLegacyText(builder, color, decorations);
        return builder.toString();
    }

    protected void toLegacyText(ChatStringBuilder builder, @Nullable ChatColor color, Set<ChatColor> decorations)
    {
        color = getColor(color);
        decorations = getDecorations(decorations);

        toLegacyTextContent(builder, color, decorations);

        if ( extra != null )
        {
            for ( BaseComponent e : extra )
            {
                e.toLegacyText( builder, color, decorations );
            }
        }
    }

    protected void toLegacyTextContent(ChatStringBuilder builder, @Nullable ChatColor color, Set<ChatColor> decorations) {
    }

    protected static final Joiner JOINER = Joiner.on(", ");

    public Boolean getDecoration(ChatColor decoration) {
        switch(decoration) {
            case BOLD: return isBoldRaw();
            case ITALIC: return isItalicRaw();
            case UNDERLINE: return isUnderlinedRaw();
            case STRIKETHROUGH: return isStrikethroughRaw();
            case MAGIC: return isObfuscatedRaw();
            default: return null;
        }
    }

    public boolean getDecoration(ChatColor decoration, boolean def) {
        Boolean flag = getDecoration(decoration);
        if(flag != null) {
            return flag;
        } else {
            return def;
        }
    }

    public void setDecoration(ChatColor decoration, Boolean flag) {
        switch(decoration) {
            case BOLD: setBold(flag); return;
            case ITALIC: setItalic(flag); return;
            case UNDERLINE: setUnderlined(flag); return;
            case STRIKETHROUGH: setStrikethrough(flag); return;
            case MAGIC: setObfuscated(flag); return;
        }
    }

    public Map<ChatColor, Boolean> getDecorations() {
        EnumMap<ChatColor, Boolean> decos = new EnumMap(ChatColor.class);
        for(ChatColor deco : ChatColor.DECORATIONS) {
            final Boolean flag = getDecoration(deco);
            if(flag != null) decos.put(deco, flag);
        }
        return decos;
    }

    public Set<ChatColor> getDecorations(Set<ChatColor> def) {
        EnumSet<ChatColor> decos = EnumSet.noneOf(ChatColor.class);
        for(ChatColor deco : ChatColor.DECORATIONS) {
            if(getDecoration(deco, def.contains(deco))) {
                decos.add(deco);
            }
        }
        return decos;
    }

    public void mergeDecorations(BaseComponent from) {
        for(ChatColor deco : ChatColor.DECORATIONS) {
            Boolean flag = from.getDecoration(deco);
            if(flag != null) setDecoration(deco, flag);
        }
    }

    public void mergeColor(BaseComponent from) {
        if(from.getColorRaw() != null) {
            setColor(from.getColorRaw());
        }
    }

    public void mergeEvents(BaseComponent from) {
        if(from.getClickEvent() != null) {
            setClickEvent(from.getClickEvent());
        }
        if(from.getHoverEvent() != null) {
            setHoverEvent(from.getHoverEvent());
        }
    }

    public void mergeFormatting(BaseComponent from) {
        mergeDecorations(from);
        mergeColor(from);
        mergeEvents(from);
    }

    /**
     * Verify that the given component can become a part of this component
     * by checking that they do not already have the inverse relationship.
     *
     * This method should be called BEFORE making the given component a child
     * of this one, so that this component's state remains valid.
     *
     * @throws IllegalArgumentException if the given component contains this component
     */
    public void validateChild(BaseComponent child) {
        Preconditions.checkNotNull(child);
        if(child.contains(this)) {
            throw new IllegalArgumentException("Component cycle detected between " + this + " and " + child);
        }
    }

    /**
     * Is the given component contained, in whole or in part, by this one?
     *
     * This method is used to detect containment cycles. Unlike with collections,
     * equality is tested with == rather than {@link #equals(Object)}.
     *
     * Subclasses with recursive fields of their own should override this method
     * to include those fields in the search.
     *
     * @return true if the given component is the same instance as this component,
     *         or is a child of this component.
     */
    public boolean contains(BaseComponent child) {
        if(this == child) return true;

        for(BaseComponent extra : getExtra()) {
            if(extra.contains(child)) return true;
        }

        if(getHoverEvent() != null) {
            for(BaseComponent value : getHoverEvent().getValue()) {
                if(value.contains(child)) return true;
            }
        }

        return false;
    }

    /**
     * Contribute leading fields to the output of {@link #toString()}.
     *
     * These fields should be relatively short i.e. not recursive. They will appear before
     * any fields contributed by {@link #toStringLast(List)}, which can make the output
     * easier to read.
     */
    protected void toStringFirst(List<String> fields) {
        if(getColorRaw() != null) {
            fields.add("color=\"" + getColorRaw().name().toLowerCase() + "\"");
        }

        for(ChatColor format : ChatColor.DECORATIONS) {
            final Boolean flag = getDecoration(format);
            if(flag != null) {
                fields.add(format.name().toLowerCase() + "=" + flag);
            }
        }

        if(getClickEvent() != null) {
            fields.add("clickEvent=" + getClickEvent());
        }
    }

    /**
     * Contribute trailing fields to the output of {@link #toString()}.
     *
     * Particularly long fields, i.e. recursive ones, should be added here.
     */
    protected void toStringLast(List<String> fields) {
        if(getHoverEvent() != null) {
            fields.add("hoverEvent=" + getHoverEvent());
        }

        if(getExtra() != null && !getExtra().isEmpty()) {
            fields.add("extra=[" + Joiner.on(", ").join(getExtra()) + "]");
        }
    }

    /**
     * Delegates to {@link #toStringFirst(List)} and {@link #toStringLast(List)}.
     */
    @Override
    public final String toString() {
        final List<String> fields = new ArrayList<String>();
        toStringFirst(fields);
        toStringLast(fields);
        return getClass().getSimpleName() + "{" + JOINER.join(fields) + "}";
    }

    /**
     * {@link #equals(Object)} delegates to this method when its argument is a {@link BaseComponent},
     * and is not null or this.
     *
     * Overrides of this method must call the supermethod in order to compare the base properties.
     * This should be done after performing any quick comparisons, and before any expensive ones,
     * as the base method contains both.
     */
    protected boolean equals(BaseComponent that) {
        return Objects.equal(this.color, that.color) &&
               Objects.equal(this.bold, that.bold) &&
               Objects.equal(this.italic, that.italic) &&
               Objects.equal(this.underlined, that.underlined) &&
               Objects.equal(this.strikethrough, that.strikethrough) &&
               Objects.equal(this.obfuscated, that.obfuscated) &&
               Objects.equal(this.insertion, that.insertion) &&
               Objects.equal(this.clickEvent, that.clickEvent) &&
               Objects.equal(this.hoverEvent, that.hoverEvent) &&
               Objects.equal(this.extra, that.extra);
    }

    /**
     * Delegates to {@link #equals(BaseComponent)}.
     */
    @Override
    public final boolean equals(Object that) {
        return that != null && (
            that == this || (
                that instanceof BaseComponent &&
                equals((BaseComponent) that)
            )
        );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(color, bold, italic, underlined, strikethrough, obfuscated, insertion, clickEvent, hoverEvent, extra);
    }
}
