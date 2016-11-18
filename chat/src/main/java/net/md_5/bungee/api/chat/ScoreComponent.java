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
public class ScoreComponent extends BaseComponent {

    @NonNull private final String name;
    @NonNull private final String objective;

    @Override
    public ScoreComponent duplicate() {
        return new ScoreComponent(getName(), getObjective());
    }

    private String plainTextContent() {
        return getName() + ':' + getObjective();
    }

    @Override
    void toPlainText(StringBuilder builder) {
        builder.append(plainTextContent());
        super.toPlainText(builder);
    }

    @Override
    protected void toLegacyTextContent(ChatStringBuilder builder, ChatColor color, Set<ChatColor> decorations) {
        builder.format(color, decorations);
        builder.append(plainTextContent());
    }

    @Override
    protected void toStringFirst(List<String> fields) {
        fields.add("name=\"" + getName() + '"');
        fields.add("objective=\"" + getObjective() + '"');
        super.toStringFirst(fields);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), name, objective);
    }

    @Override
    protected boolean equals(BaseComponent that) {
        return that instanceof ScoreComponent &&
               name.equals(((ScoreComponent) that).getName()) &&
               objective.equals(((ScoreComponent) that).getObjective()) &&
               super.equals(that);
    }
}
