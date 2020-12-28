package csp;

/**
 * Row-Constraint: (There can only be x-trees in tree)
 * Column-Constraint: (There can only be x-trees in column)
 * TreeHorizontalorVerticallyAdjecent-Constraint: (The Variable can only be Tent if a tree is vertially or horizontally attached to the cell)
 * NoTentsNextToEachOther-Constraint: (No Tent can be next (horizontally, vertically, diagonally) to antoher Tent)
 * EachTentIsAttachedToOneTree-Constraint: (So there as as many tents as there are trees)
 * OneTreePerTent: (A tree might be next to two tents, but is only connected to one)
 */
public class AbstractConstraint {
}
