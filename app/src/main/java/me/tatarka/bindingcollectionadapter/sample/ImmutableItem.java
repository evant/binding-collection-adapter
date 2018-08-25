package me.tatarka.bindingcollectionadapter.sample;

public class ImmutableItem {
    public final int index;
    public final boolean checked;

    public ImmutableItem(int index, boolean checked) {
        this.index = index;
        this.checked = checked;
    }

    public ImmutableItem withChecked(boolean checked) {
        return new ImmutableItem(index, checked);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImmutableItem that = (ImmutableItem) o;
        if (index != that.index) return false;
        return checked == that.checked;
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + (checked ? 1 : 0);
        return result;
    }
}
