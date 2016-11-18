package net.md_5.bungee.api.chat;

import java.util.List;
import java.util.Set;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatStringBuilder;

@Getter
@AllArgsConstructor
public class SelectorComponent extends BaseComponent {

    @NonNull private final String selector;

    @Override
    public SelectorComponent duplicate() {
        return new SelectorComponent(getSelector());
    }

    @Override
    void toPlainText(StringBuilder builder) {
        builder.append(selector);
        super.toPlainText(builder);
    }

    @Override
    protected void toLegacyTextContent(ChatStringBuilder builder, ChatColor color, Set<ChatColor> decorations) {
        builder.format(color, decorations);
        builder.append(getSelector());
    }

    @Override
    protected void toStringFirst(List<String> fields) {
        fields.add("selector=\"" + getSelector() + '"');
        super.toStringFirst(fields);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), selector);
    }

    @Override
    protected boolean equals(BaseComponent that) {
        return that instanceof SelectorComponent &&
               selector.equals(((SelectorComponent) that).getSelector()) &&
               super.equals(that);
    }
}
