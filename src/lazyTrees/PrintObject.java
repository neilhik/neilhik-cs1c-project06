package lazyTrees;

public class PrintObject<E> implements Printable<E> {
    public void print(E x){
        System.out.print(x + " ");
    }
}
