package model


class Neighbors {
    private int of

    public Neighbors(int of) {
        this.of = of
    }

    Optional<Integer> getLeft() {
        if (of % 10 == 0)
            Optional.empty()
        else
            Optional.of(of - 1)
    }

    Optional<Integer> getRight() {
        if ((of - 9) % 10 == 0)
            Optional.empty()
        else
            Optional.of(of + 1)
    }

    Optional<Integer> getUpper() {
        if ((of < 10))
            Optional.empty()
        else
            Optional.of(of + 1)
    }
    Optional<Integer> getLower() {
        if ((of > 89))
            Optional.empty()
        else
            Optional.of(of + 1)
    }

    Set<Integer> getAll(){
        Set<Integer> all = new HashSet<>()

        left.isPresent()?all.add(left.get()):{}
        right.isPresent()?all.add(right.get()):{}
        upper.isPresent()?all.add(upper.get()):{}
        lower.isPresent()?all.add(lower.get()):{}

        all
    }
}
