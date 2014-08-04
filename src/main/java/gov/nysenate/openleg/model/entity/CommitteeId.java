package gov.nysenate.openleg.model.entity;

import com.google.common.collect.ComparisonChain;

import java.io.Serializable;
import java.util.Objects;

public class CommitteeId implements Serializable, Comparable<CommitteeId>
{
    private static final long serialVersionUID = -4463517719215556844L;

    /** The chamber this committee belongs to. */
    protected Chamber chamber;

    /** The official name of this committee according to LBDC. */
    protected String name;

    /* --- Constructors --- */

    public CommitteeId(Chamber chamber, String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null!");
        }
        if (chamber == null) {
            throw new IllegalArgumentException("Chamber cannot be null!");
        }
        this.chamber = chamber;
        this.name = name;
    }

    /* --- Overrides --- */

    @Override
    public String toString() {
        return chamber.toString() + "-" + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final CommitteeId other = (CommitteeId) obj;
        return Objects.equals(this.chamber, other.chamber) &&
               Objects.equals(this.name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chamber, name);
    }

    @Override
    public int compareTo(CommitteeId o) {
        return ComparisonChain.start()
            .compare(this.getChamber().name(), o.getChamber().name())
            .compare(this.getName(), o.getName())
            .result();
    }

    /* --- Basic Getters/Setters --- */

    public Chamber getChamber() {
        return chamber;
    }

    public String getName() {
        return name;
    }
}
